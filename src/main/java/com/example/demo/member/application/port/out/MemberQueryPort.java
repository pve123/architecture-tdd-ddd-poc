package com.example.demo.member.application.port.out;

import com.example.demo.member.adapter.in.web.request.MemberSearchRequest;
import com.example.demo.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryPort {


    Page<Member> searchMembers(Pageable pageable, MemberSearchRequest memberSearchRequest);

    Member findById(String id);

    boolean existsByEmail(String email);

    /** 이메일을 바꿀 때, 같은 이메일을 다른 회원이 쓰고 있는지 확인합니다. */
    boolean existsByEmailOfOtherMember(String email, String memberId);

}
