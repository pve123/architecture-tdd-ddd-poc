package com.example.demo.member.application.port.out;

import com.example.demo.member.adapter.in.web.request.MemberSearchRequest;
import com.example.demo.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryPort {


    Page<Member> searchMembers(Pageable pageable, MemberSearchRequest memberSearchRequest);

    Member findById(String id);

}
