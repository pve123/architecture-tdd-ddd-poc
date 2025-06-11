package com.example.demo.member.adapter.in.web.response;

import com.example.demo.member.domain.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "회원 생성 응답")
public record CreateMemberResponse(

        @Schema(description = "ULID 기반 식별자", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
        String id,
        @Schema(description = "이메일", example = "user@example.com")
        String email,
        @Schema(description = "회원 이름", example = "홍길동")
        String name,
        @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
        GenderEnum gender,
        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
        String address,
        @Schema(description = "생성일", example = "2025-01-01T15:55:55")
        LocalDateTime createdAt,
        @Schema(description = "수정일", example = "2023-12-10T55:55:55")
        LocalDateTime updatedAt
) {


}
