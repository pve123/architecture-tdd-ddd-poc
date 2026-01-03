package com.example.demo.member.application.port.in;

import com.example.demo.member.domain.Member;

public interface GetMemberUseCase {

    Member getMember(String id);
}
