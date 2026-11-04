package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.member.application.port.in.CreateMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.DeleteMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.UpdateMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 API의 입구(입력 어댑터)입니다.
 * 내 정보는 요청 본문이나 주소의 ID가 아니라 토큰으로 정해진 회원(memberId)의 것만 다룹니다.
 */
@Tag(name = "회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberRestController {

    private final CreateMemberUseCase createMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;
    private final UpdateMemberUseCase updateMemberUseCase;
    private final DeleteMemberUseCase deleteMemberUseCase;

    @Operation(summary = "회원 가입")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@Valid @RequestBody MemberRequest.Create request) {
        return MemberResponse.from(createMemberUseCase.createMember(request.toCommand()));
    }

    @Operation(summary = "내 정보 보기")
    @GetMapping("/me")
    public MemberResponse getMe(@AuthenticationPrincipal String memberId) {
        return MemberResponse.from(getMemberUseCase.getMember(memberId));
    }

    @Operation(summary = "내 연락처 고치기", description = "비워 둔 값은 바꾸지 않습니다.")
    @PatchMapping("/me")
    public MemberResponse updateMe(@AuthenticationPrincipal String memberId,
                                   @Valid @RequestBody MemberRequest.Update request) {
        return MemberResponse.from(updateMemberUseCase.updateMember(request.toCommand(memberId)));
    }

    @Operation(summary = "탈퇴하기")
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@AuthenticationPrincipal String memberId) {
        deleteMemberUseCase.deleteMember(memberId);
    }
}
