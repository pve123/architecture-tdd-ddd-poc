package com.example.demo.member.application.port.out;

import com.example.demo.member.domain.Member;

public interface MemberCommandPort {


    void softDeleteById(String id);

    Member save(Member member);

    Member update(Member member);
}
