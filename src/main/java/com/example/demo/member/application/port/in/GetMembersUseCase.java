package com.example.demo.member.application.port.in;

import com.example.demo.member.adapter.in.web.request.MemberSearchRequest;
import com.example.demo.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetMembersUseCase {

    Page<Member> getMembers(Pageable pageable, MemberSearchRequest memberSearchRequest);

}
