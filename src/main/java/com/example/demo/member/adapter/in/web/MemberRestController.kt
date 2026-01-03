package com.example.demo.member.adapter.`in`.web

import com.example.demo.member.adapter.`in`.web.request.CreateMemberRequest
import com.example.demo.member.adapter.`in`.web.request.UpdateMemberRequest
import com.example.demo.member.adapter.`in`.web.response.CreateMemberResponse
import com.example.demo.member.adapter.`in`.web.response.GetMemberResponse
import com.example.demo.member.adapter.`in`.web.response.UpdateMemberResponse
import com.example.demo.member.application.port.`in`.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "사용자", description = "사용자 생성/조회/수정/삭제")
@RestController
@RequestMapping("/v1/member")
class MemberRestController(
    private val createMemberUseCase: CreateMemberUseCase,
    private val getMemberUseCase: GetMemberUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
    private val getMembersUseCase: GetMembersUseCase,
    private val memberWebMapper: MemberWebMapper
) {

    @Operation(
        summary = "사용자 목록 조회 (페이징)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 목록 조회 (페이징) 완료",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = GetMemberResponse::class))
                )]
            )
        ]
    )
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    fun getMembers(pageable: Pageable): Page<GetMemberResponse> =
        getMembersUseCase.getMembers(pageable)
            .map(memberWebMapper::toGetMemberResponse)

    @Operation(
        summary = "사용자 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 조회 완료",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GetMemberResponse::class)
                )]
            )
        ]
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getMember(
        @Parameter(
            name = "id",
            description = "회원 고유 ID",
            example = "01HZY74JZP5VDFKHX6D5YFRAZW"
        )
        @RequestParam id: String
    ): GetMemberResponse =
        memberWebMapper.toGetMemberResponse(
            getMemberUseCase.getMember(id)
        )

    @Operation(
        summary = "사용자 생성",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateMemberRequest::class)
            )]
        ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "사용자 생성 완료",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CreateMemberResponse::class)
                )]
            )
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMember(
        @Valid @RequestBody request: CreateMemberRequest
    ): CreateMemberResponse {
        val command = memberWebMapper.toCreateCommand(request)
        val result = createMemberUseCase.createMember(command)
        return memberWebMapper.toCreateMemberResponse(result)
    }

    @Operation(
        summary = "사용자 수정",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UpdateMemberRequest::class)
            )]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 수정 완료",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UpdateMemberResponse::class)
                )]
            )
        ]
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateMember(
        @Parameter(
            description = "회원 고유 ID",
            example = "01HZY74JZP5VDFKHX6D5YFRAZW"
        )
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateMemberRequest
    ): UpdateMemberResponse {
        val command = memberWebMapper.toUpdateCommand(id, request)
        val result = updateMemberUseCase.updateMember(command)
        return memberWebMapper.toUpdateMemberResponse(result)
    }

    @Operation(
        summary = "사용자 삭제",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "사용자 삭제 완료"
            )
        ]
    )
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember(
        @Parameter(
            name = "id",
            description = "회원 고유 ID",
            example = "01HZY74JZP5VDFKHX6D5YFRAZW"
        )
        @RequestParam id: String
    ) {
        deleteMemberUseCase.deleteMember(id)
    }
}
