package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.member.domain.Gender;
import io.github.pve123.hexagonal.member.domain.Member;

import java.time.LocalDateTime;

/** 회원 응답입니다. 비밀번호와 탈퇴 정보는 담지 않습니다. */
public record MemberResponse(
        String id,
        String email,
        String name,
        Gender gender,
        String phoneNumber,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getGender(),
                member.getPhoneNumber(),
                member.getAddress(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }
}
