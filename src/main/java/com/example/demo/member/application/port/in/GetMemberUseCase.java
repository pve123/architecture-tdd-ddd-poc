package com.example.demo.member.application.port.in;

import com.example.demo.member.domain.Member;

import java.util.List;

public interface GetMemberUseCase {

    Member getMember(String id);
    List<Member> getAllMember();
}
