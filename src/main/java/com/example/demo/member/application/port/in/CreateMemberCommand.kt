package com.example.demo.member.application.port.`in`

import com.example.demo.member.domain.GenderEnum
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "사용자 생성 입력 모델")
data class CreateMemberCommand(
    val email: String,
    val password: String,
    val name: String,
    val gender: GenderEnum,
    val phoneNumber: String,
    val address: String
)
