package io.github.pve123.hexagonal.member.application.service;

import io.github.pve123.hexagonal.member.adapter.in.web.request.MemberSearchRequest;
import io.github.pve123.hexagonal.member.application.port.in.*;
import io.github.pve123.hexagonal.member.application.port.out.MemberCommandPort;
import io.github.pve123.hexagonal.member.application.port.out.MemberQueryPort;
import io.github.pve123.hexagonal.common.exception.BusinessException;
import io.github.pve123.hexagonal.common.exception.MemberErrorCodeEnum;
import io.github.pve123.hexagonal.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements GetMembersUseCase, GetMemberUseCase, CreateMemberUseCase, UpdateMemberUseCase, DeleteMemberUseCase {

    private final MemberQueryPort memberQueryPort;
    private final MemberCommandPort memberCommandPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<Member> getMembers(Pageable pageable, MemberSearchRequest memberSearchRequest) {
        Page<Member> resultMembers = memberQueryPort.searchMembers(pageable, memberSearchRequest);
        return resultMembers;
    }

    @Override
    public Member getMember(String id) {
        Member resultMember = memberQueryPort.findById(id);
        return resultMember;
    }

    @Override
    public Member createMember(Member member) {
        if (memberQueryPort.existsByEmail(member.getEmail())) {
            throw new BusinessException(MemberErrorCodeEnum.EMAIL_DUPLICATED);
        }
        //비밀번호 BCrypt 인코딩
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member = member.toBuilder().password(encodedPassword).build();
        Member resultMember = memberCommandPort.save(member);
        return resultMember;
    }

    @Override
    public Member updateMember(Member member) {
        if (member.getEmail() != null && memberQueryPort.existsByEmailOfOtherMember(member.getEmail(), member.getId())) {
            throw new BusinessException(MemberErrorCodeEnum.EMAIL_DUPLICATED);
        }
        Member updateMember = memberCommandPort.update(member);
        return updateMember;
    }

    @Override
    public void deleteMember(String id) {
        memberCommandPort.softDeleteById(id);
    }
}
