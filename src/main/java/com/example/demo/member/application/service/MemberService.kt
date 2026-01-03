package com.example.demo.member.application.service;

import com.example.demo.member.application.port.in.*;
import com.example.demo.member.application.port.out.*;
import com.example.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements CreateMemberUseCase, GetMemberUseCase, DeleteMemberUseCase, UpdateMemberUseCase, QueryMemberUseCase {

    private final CreateMemberPort createMemberPort;
    private final GetMemberPort getMemberPort;
    private final DeleteMemberPort deleteMemberPort;
    private final UpdateMemberPort updateMemberPort;
    private final QueryMemberPort queryMemberPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member createMember(Member member) {
        //비밀번호 BCrypt 인코딩
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member = member.toBuilder().password(encodedPassword).build();
        Member resultMember = createMemberPort.save(member);
        return resultMember;
    }


    @Override
    public Page<Member> getMembers(Pageable pageable) {
        Page<Member> resultMembers = queryMemberPort.findAllByPage(pageable);
        return resultMembers;
    }

    @Override
    public Member getMember(String id) {
        Member resultMember = getMemberPort.findById(id);
        return resultMember;
    }

    @Override
    public void deleteMember(String id) {
        deleteMemberPort.softDeleteById(id);
    }

    @Override
    public Member updateMember(Member member) {
        Member updateMember = updateMemberPort.update(member);
        return updateMember;
    }
}
