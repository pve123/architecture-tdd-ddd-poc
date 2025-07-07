package com.example.demo.member.application.port.in;

import com.example.demo.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryMemberUseCase {

    Page<Member> getMembers(Pageable pageable);

}
