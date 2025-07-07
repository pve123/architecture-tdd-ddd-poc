package com.example.demo.member.adapter.in.web;


import com.example.demo.common.response.CommonResponse;
import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.application.port.in.CreateMemberUseCase;
import com.example.demo.member.application.port.in.DeleteMemberUseCase;
import com.example.demo.member.application.port.in.GetMemberUseCase;
import com.example.demo.member.application.port.in.QueryMemberUseCase;
import com.example.demo.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 생성/조회/수정/삭제")
@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final CreateMemberUseCase createMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;
    private final DeleteMemberUseCase deleteMemberUseCase;
    private final MemberWebMapper memberWebMapper;
    private final QueryMemberUseCase queryMemberUseCase;

    @Operation(
            summary = "사용자 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateMemberRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "사용자 생성 완료",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CommonResponse.class)
                                    ),
                            }
                    ),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse createMember(@Valid @RequestBody CreateMemberRequest createMemberRequest) {
        Member member = memberWebMapper.toDomain(createMemberRequest);
        Member resultMember = createMemberUseCase.createMember(member);
        CreateMemberResponse createMemberResponse = memberWebMapper.toCreateMemberResponse(resultMember);
        CommonResponse commonResponse = new CommonResponse<>("사용자 생성이 정상적으로 처리됐습니다", createMemberResponse);
        return commonResponse;
    }


    @Operation(
            summary = "사용자 목록 조회 (페이징)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 목록 조회 (페이징) 완료",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = GetMemberResponse.class))
                                    ),
                            }
                    ),
            }
    )
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetMemberResponse> getAllMember(Pageable pageable) {

        Page<Member> resultMembers = queryMemberUseCase.getMembers(pageable);
        Page<GetMemberResponse> resultGetMemberResponses = resultMembers.map(memberWebMapper::toGetMemberResponse);
        return resultGetMemberResponses;
    }

    @Operation(
            summary = "사용자 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 조회 완료",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GetMemberResponse.class)
                                    ),
                            }
                    ),
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetMemberResponse getMember(@Parameter(name = "id", description = "회원 고유 ID", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
                                       @RequestParam String id) {
        Member resultMember = getMemberUseCase.getMember(id);
        GetMemberResponse getMemberResponse = memberWebMapper.toGetMemberResponse(resultMember);
        return getMemberResponse;
    }

    @Operation(
            summary = "사용자 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "사용자 삭제 완료"
                    ),
            }
    )
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@Parameter(name = "id", description = "회원 고유 ID", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
                             @RequestParam String id) {
        deleteMemberUseCase.deleteMember(id);
    }

}
