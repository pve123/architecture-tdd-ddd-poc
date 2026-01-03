package com.example.demo.member.application.port.`in`

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "사용자 업데이트 입력 모델")
data class UpdateMemberCommand(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val address: String?
)
