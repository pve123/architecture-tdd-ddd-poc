package com.example.demo.member.adapter.in.web;

import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWebMapper {

    Member toDomain(CreateMemberRequest createMemberRequest);

    CreateMemberResponse toCreateMemberResponse(Member member);

    GetMemberResponse toGetMemberResponse(Member member);

    default Member toDomain(String memberId) {
        return Member.builder()
                .id(memberId)
                .build();
    }
}
