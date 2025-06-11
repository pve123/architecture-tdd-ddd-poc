package com.example.demo.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder(toBuilder = true)
@Getter
@Schema(description = "사용자 도메인")
public class Member {


    @Schema(description = "ULID 기반 식별자", example = "01HZY74JZP5VDFKHX6D5YFRAZW")
    private String id;
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    @Schema(description = "비밀번호", example = "qwer123456789!")
    private String password;
    @Schema(description = "회원 이름", example = "홍길동")
    private String name;
    @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    private GenderEnum gender;
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;
    @Schema(description = "삭제유무", example = "false", allowableValues = {"true", "false"})
    private Boolean isDeleted;
    @Schema(description = "삭제일", example = "2025-01-01T15:55:55")
    private LocalDateTime deletedAt;
    @Schema(description = "생성일", example = "2025-01-01T15:55:55")
    private LocalDateTime createdAt;
    @Schema(description = "수정일", example = "2023-12-10T55:55:55")
    private LocalDateTime updatedAt;

}
