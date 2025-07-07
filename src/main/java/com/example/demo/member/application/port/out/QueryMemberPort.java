package com.example.demo.member.application.port.out;

import com.example.demo.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryMemberPort {

    Page<Member> findAllByPage(Pageable pageable);
}
