package com.example.demo.member.domain

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "사용자 도메인")
data class Member(

    @Schema(
        description = "ULID 기반 식별자",
        example = "01HZY74JZP5VDFKHX6D5YFRAZW"
    )
    val id: String,

    @Schema(description = "이메일", example = "user@example.com")
    val email: String,

    @Schema(description = "비밀번호", example = "qwer123456789!")
    val password: String,

    @Schema(description = "회원 이름", example = "홍길동")
    val name: String,

    @Schema(
        description = "성별",
        example = "MALE",
        allowableValues = ["MALE", "FEMALE"]
    )
    val gender: GenderEnum,

    @Schema(description = "전화번호", example = "010-1234-5678")
    val phoneNumber: String,

    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    val address: String,

    @Schema(
        description = "삭제유무",
        example = "false",
        allowableValues = ["true", "false"]
    )
    val isDeleted: Boolean = false,

    @Schema(description = "삭제일", example = "2025-01-01T15:55:55")
    val deletedAt: LocalDateTime? = null,

    @Schema(description = "생성일", example = "2025-01-01T15:55:55")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Schema(description = "수정일", example = "2023-12-10T15:55:55")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
