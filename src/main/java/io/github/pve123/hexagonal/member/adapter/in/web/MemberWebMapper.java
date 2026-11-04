package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.member.adapter.in.web.request.CreateMemberRequest;
import io.github.pve123.hexagonal.member.adapter.in.web.request.UpdateMemberRequest;
import io.github.pve123.hexagonal.member.adapter.in.web.response.CreateMemberResponse;
import io.github.pve123.hexagonal.member.adapter.in.web.response.GetMemberResponse;
import io.github.pve123.hexagonal.member.adapter.in.web.response.UpdateMemberResponse;
import io.github.pve123.hexagonal.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberWebMapper {

    Member toDomain(CreateMemberRequest createMemberRequest);

    CreateMemberResponse toCreateMemberResponse(Member member);

    GetMemberResponse toGetMemberResponse(Member member);

    UpdateMemberResponse toUpdateMemberResponse(Member member);

    default Member toDomain(String memberId) {
        return Member.builder()
                .id(memberId)
                .build();
    }

    default Member toDomain(String memberId, UpdateMemberRequest updateMemberRequest) {
        return Member.builder()
                .id(memberId)
                .email(updateMemberRequest.email())
                .address(updateMemberRequest.address())
                .phoneNumber(updateMemberRequest.phoneNumber())
                .build();
    }

}
