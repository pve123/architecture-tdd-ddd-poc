package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.common.web.PageResponse;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 회원의 연락처와 주소가 담긴 목록이라 관리자만 볼 수 있습니다. */
@Tag(name = "관리자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/members")
public class AdminMemberController {

    private final GetMembersUseCase getMembersUseCase;
    private final GetMemberUseCase getMemberUseCase;

    @Operation(summary = "회원 목록 (검색, 페이징, 정렬)", description = "sort는 name, email, createdAt만 받습니다.")
    @GetMapping
    public PageResponse<MemberResponse> getMembers(@ParameterObject Pageable pageable,
                                                   @ParameterObject MemberRequest.Search search) {
        return PageResponse.from(getMembersUseCase.getMembers(search.toCondition(), pageable).map(MemberResponse::from));
    }

    @Operation(summary = "회원 한 명 보기")
    @GetMapping("/{memberId}")
    public MemberResponse getMember(@PathVariable String memberId) {
        return MemberResponse.from(getMemberUseCase.getMember(memberId));
    }
}
