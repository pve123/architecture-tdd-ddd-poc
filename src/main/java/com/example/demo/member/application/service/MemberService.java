package com.example.demo.member.application.service;

import com.example.demo.member.application.port.in.CreateMemberUseCase;
import com.example.demo.member.application.port.in.DeleteMemberUseCase;
import com.example.demo.member.application.port.in.GetMemberUseCase;
import com.example.demo.member.application.port.out.CreateMemberPort;
import com.example.demo.member.application.port.out.DeleteMemberPort;
import com.example.demo.member.application.port.out.GetMemberPort;
import com.example.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements CreateMemberUseCase, GetMemberUseCase, DeleteMemberUseCase {

    private final CreateMemberPort createMemberPort;
    private final GetMemberPort getMemberPort;
    private final DeleteMemberPort deleteMemberPort;
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
    public Member getMember(String id) {
        Member resultMember = getMemberPort.findById(id);
        return resultMember;
    }

    @Override
    public List<Member> getAllMember() {
        List<Member> memberList = getMemberPort.findAll();
        return memberList;
    }

    @Override
    public void deleteMember(String id) {
        deleteMemberPort.deleteById(id);
    }
}
