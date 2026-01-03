package com.example.demo.member.adapter.`in`.web.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


@Schema(description = "회원 수정 요청")
data class UpdateMemberRequest(

    @field:NotBlank(message = "이메일은 필수입니다.")
    @field:Size(min = 5, max = 254, message = "이메일은 5자 이상 254자 이하여야 합니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    @field:Schema(description = "이메일", example = "user@example.com")
    val email: String?,

    @field:NotBlank(message = "전화번호는 필수입니다.")
    @field:Size(min = 10, max = 20, message = "전화번호는 10자 이상 15자 이하여야 합니다.")
    @field:Pattern(
        regexp = "^010-\\d{4}-\\d{4}\$",
        message = "한국 형식"
    )
    @field:Schema(description = "전화번호", example = "010-1234-5678")
    val phoneNumber: String?,

    @field:NotBlank(message = "주소는 필수입니다.")
    @field:Size(min = 10, max = 200, message = "주소는 10자 이상 200자 이하여야 합니다.")
    @field:Pattern(message = "한글, 영문, 숫자, 일반 구두점 허용", regexp = "^[가-힣a-zA-Z0-9\\s,.\\-()#]{10,200}$")
    @field:Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
    val address: String?

)
