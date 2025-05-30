package com.example.demo.member.application.port.in;

import com.example.demo.member.domain.Member;

public interface CreateMemberUseCase {

    Member createMember(Member member);
}
