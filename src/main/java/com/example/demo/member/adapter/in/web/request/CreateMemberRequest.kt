package com.example.demo.member.adapter.`in`.web.request

import com.example.demo.member.domain.GenderEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

@Schema(description = "회원 생성 요청")
data class CreateMemberRequest(

    @field:NotBlank(message = "이메일은 필수입니다.")
    @field:Size(min = 5, max = 254, message = "이메일은 5자 이상 254자 이하여야 합니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    @Schema(description = "이메일", example = "user@example.com")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    @field:Size(min = 8, max = 128, message = "비밀번호는 8자 이상 128자 이하여야 합니다.")
    @field:Pattern(
        message = "최소 하나의 대문자, 소문자, 숫자, 특수문자 포함",
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$"
    )
    @Schema(description = "비밀번호", example = "QWERasdf1234!")
    val password: String,

    @field:NotBlank(message = "이름은 필수입니다.")
    @field:Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다.")
    @field:Pattern(
        message = "한글, 영문, 공백 허용",
        regexp = "^[가-힣a-zA-Z\\s]{2,50}$"
    )
    @Schema(description = "회원 이름", example = "홍길동")
    val name: String,

    @field:NotNull(message = "성별은 필수입니다.")
    @Schema(
        description = "성별",
        example = "MALE",
        allowableValues = ["MALE", "FEMALE"]
    )
    val gender: GenderEnum,

    @field:NotBlank(message = "전화번호는 필수입니다.")
    @field:Size(min = 10, max = 20, message = "전화번호는 10자 이상 15자 이하여야 합니다.")
    @field:Pattern(
        message = "한국 형식",
        regexp = "^010-\\d{4}-\\d{4}$"
    )
    @Schema(description = "전화번호", example = "010-1234-5678")
    val phoneNumber: String,

    @field:NotBlank(message = "주소는 필수입니다.")
    @field:Size(min = 10, max = 200, message = "주소는 10자 이상 200자 이하여야 합니다.")
    @field:Pattern(
        message = "한글, 영문, 숫자, 일반 구두점 허용",
        regexp = "^[가-힣a-zA-Z0-9\\s,.\\-()#]{10,200}$"
    )
    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    val address: String
)
