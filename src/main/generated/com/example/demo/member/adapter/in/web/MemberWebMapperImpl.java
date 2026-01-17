package com.example.demo.member.adapter.in.web;

import com.example.demo.member.adapter.in.web.request.CreateMemberRequest;
import com.example.demo.member.adapter.in.web.response.CreateMemberResponse;
import com.example.demo.member.adapter.in.web.response.GetMemberResponse;
import com.example.demo.member.adapter.in.web.response.UpdateMemberResponse;
import com.example.demo.member.domain.GenderEnum;
import com.example.demo.member.domain.Member;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-30T14:38:54+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Amazon.com Inc.)"
)
@Component
public class MemberWebMapperImpl implements MemberWebMapper {

    @Override
    public Member toDomain(CreateMemberRequest createMemberRequest) {
        if ( createMemberRequest == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.email( createMemberRequest.email() );
        member.password( createMemberRequest.password() );
        member.name( createMemberRequest.name() );
        member.gender( createMemberRequest.gender() );
        member.phoneNumber( createMemberRequest.phoneNumber() );
        member.address( createMemberRequest.address() );

        return member.build();
    }

    @Override
    public CreateMemberResponse toCreateMemberResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        String id = null;
        String email = null;
        String name = null;
        GenderEnum gender = null;
        String phoneNumber = null;
        String address = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = member.getId();
        email = member.getEmail();
        name = member.getName();
        gender = member.getGender();
        phoneNumber = member.getPhoneNumber();
        address = member.getAddress();
        createdAt = member.getCreatedAt();
        updatedAt = member.getUpdatedAt();

        CreateMemberResponse createMemberResponse = new CreateMemberResponse( id, email, name, gender, phoneNumber, address, createdAt, updatedAt );

        return createMemberResponse;
    }

    @Override
    public GetMemberResponse toGetMemberResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        String id = null;
        String email = null;
        String name = null;
        GenderEnum gender = null;
        String phoneNumber = null;
        String address = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = member.getId();
        email = member.getEmail();
        name = member.getName();
        gender = member.getGender();
        phoneNumber = member.getPhoneNumber();
        address = member.getAddress();
        createdAt = member.getCreatedAt();
        updatedAt = member.getUpdatedAt();

        GetMemberResponse getMemberResponse = new GetMemberResponse( id, email, name, gender, phoneNumber, address, createdAt, updatedAt );

        return getMemberResponse;
    }

    @Override
    public UpdateMemberResponse toUpdateMemberResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        String id = null;
        String email = null;
        String name = null;
        GenderEnum gender = null;
        String phoneNumber = null;
        String address = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = member.getId();
        email = member.getEmail();
        name = member.getName();
        gender = member.getGender();
        phoneNumber = member.getPhoneNumber();
        address = member.getAddress();
        createdAt = member.getCreatedAt();
        updatedAt = member.getUpdatedAt();

        UpdateMemberResponse updateMemberResponse = new UpdateMemberResponse( id, email, name, gender, phoneNumber, address, createdAt, updatedAt );

        return updateMemberResponse;
    }
}
