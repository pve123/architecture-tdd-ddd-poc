package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.member.application.port.in.CreateMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.DeleteMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase;
import io.github.pve123.hexagonal.member.application.port.in.UpdateMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 회원 API의 입구(입력 어댑터)입니다. 요청을 유스케이스에 넘기고 결과를 응답으로 바꾸는 일만 합니다. */
@Tag(name = "사용자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberRestController {

    private final CreateMemberUseCase createMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;
    private final GetMembersUseCase getMembersUseCase;
    private final UpdateMemberUseCase updateMemberUseCase;
    private final DeleteMemberUseCase deleteMemberUseCase;

    @Operation(summary = "회원 목록 (검색, 페이징, 정렬)")
    @GetMapping("/page")
    public Page<MemberResponse> getMembers(@ParameterObject Pageable pageable,
                                           @ParameterObject MemberRequest.Search search) {
        return getMembersUseCase.getMembers(search.toCondition(), pageable).map(MemberResponse::from);
    }

    @Operation(summary = "회원 한 명 보기")
    @GetMapping
    public MemberResponse getMember(@RequestParam String id) {
        return MemberResponse.from(getMemberUseCase.getMember(id));
    }

    @Operation(summary = "회원 가입")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@Valid @RequestBody MemberRequest.Create request) {
        return MemberResponse.from(createMemberUseCase.createMember(request.toCommand()));
    }

    @Operation(summary = "회원 정보 고치기")
    @PutMapping("/{id}")
    public MemberResponse updateMember(@PathVariable String id, @Valid @RequestBody MemberRequest.Update request) {
        return MemberResponse.from(updateMemberUseCase.updateMember(request.toCommand(id)));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@RequestParam String id) {
        deleteMemberUseCase.deleteMember(id);
    }
}
