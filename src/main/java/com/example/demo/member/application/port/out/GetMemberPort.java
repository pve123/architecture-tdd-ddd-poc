package com.example.demo.member.application.port.out;

import com.example.demo.member.domain.Member;

import java.util.List;

public interface GetMemberPort {

    Member findById(String id);

    List<Member> findAll();
}
