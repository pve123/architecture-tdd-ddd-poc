package io.github.pve123.hexagonal.member.application.service;

import io.github.pve123.hexagonal.common.error.BusinessException;
import io.github.pve123.hexagonal.member.application.port.in.CheckActiveMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.CreateMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.DeleteMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberProfilesUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase;
import io.github.pve123.hexagonal.member.application.port.in.LoginUseCase;
import io.github.pve123.hexagonal.member.application.port.in.UpdateMemberUseCase;
import io.github.pve123.hexagonal.member.application.port.out.MemberCommandPort;
import io.github.pve123.hexagonal.member.application.port.out.MemberQueryPort;
import io.github.pve123.hexagonal.member.application.port.out.TokenIssuerPort;
import io.github.pve123.hexagonal.member.domain.Member;
import io.github.pve123.hexagonal.member.domain.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 회원 유스케이스를 처리합니다.
 * 저장소에서 회원을 꺼내 도메인에게 일을 시키고 다시 저장하는 흐름만 맡고, 규칙은 Member가 지킵니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements CreateMemberUseCase, GetMemberUseCase, GetMembersUseCase,
        UpdateMemberUseCase, DeleteMemberUseCase, CheckActiveMemberUseCase, LoginUseCase,
        GetMemberProfilesUseCase {

    private final MemberQueryPort memberQueryPort;
    private final MemberCommandPort memberCommandPort;
    private final TokenIssuerPort tokenIssuerPort;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Override
    @Transactional
    public Member createMember(CreateMemberUseCase.Command command) {
        if (memberQueryPort.existsByEmail(command.email())) {
            throw new BusinessException(MemberErrorCode.EMAIL_DUPLICATED);
        }
        Member member = Member.register(
                command.email(),
                passwordEncoder.encode(command.rawPassword()),
                command.name(),
                command.gender(),
                command.phoneNumber(),
                command.address());
        return memberCommandPort.save(member);
    }

    @Override
    public Member getMember(String memberId) {
        return findActiveMember(memberId);
    }

    @Override
    public Page<Member> getMembers(SearchCondition condition, Pageable pageable) {
        return memberQueryPort.search(condition, pageable);
    }

    @Override
    @Transactional
    public Member updateMember(UpdateMemberUseCase.Command command) {
        if (command.email() != null && memberQueryPort.existsByEmailOfOtherMember(command.email(), command.memberId())) {
            throw new BusinessException(MemberErrorCode.EMAIL_DUPLICATED);
        }
        Member member = findActiveMember(command.memberId());
        member.changeContact(command.email(), command.phoneNumber(), command.address());
        return memberCommandPort.save(member);
    }

    @Override
    @Transactional
    public void deleteMember(String memberId) {
        Member member = findActiveMember(memberId);
        member.withdraw(LocalDateTime.now(clock));
        memberCommandPort.save(member);
    }

    @Override
    public Token login(LoginUseCase.Command command) {
        // 이메일이 없을 때와 비밀번호가 틀릴 때 같은 오류를 내서, 가입 여부를 알려 주지 않습니다.
        Member member = memberQueryPort.findActiveByEmail(command.email())
                .filter(found -> passwordEncoder.matches(command.rawPassword(), found.getPassword()))
                .orElseThrow(() -> new BusinessException(MemberErrorCode.LOGIN_FAILED));
        return tokenIssuerPort.issue(member.getId(), member.getRole());
    }

    @Override
    public List<Profile> getProfiles(Collection<String> memberIds) {
        return memberQueryPort.findAllByIds(memberIds).stream()
                .map(member -> new Profile(member.getId(), member.getName(), member.isDeleted()))
                .toList();
    }

    @Override
    public void checkActive(String memberId) {
        memberQueryPort.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND))
                .checkActive();
    }

    private Member findActiveMember(String memberId) {
        return memberQueryPort.findActiveById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
