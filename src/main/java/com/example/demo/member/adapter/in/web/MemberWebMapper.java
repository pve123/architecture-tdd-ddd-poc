package com.example.demo.member.adapter.in.web;

import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWebMapper {

    // Request ↔ Domain
    Member toDomain(CreateMemberRequest createMemberRequest);

    // Domain ↔ Response
    CreateMemberResponse toCreateMemberResponse(Member member);

    // Domain ↔ Response
    GetMemberResponse toGetMemberResponse(Member member);
}
