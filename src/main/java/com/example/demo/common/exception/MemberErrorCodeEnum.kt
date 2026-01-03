package com.example.demo.common.exception

import org.springframework.http.HttpStatus


enum class MemberErrorCodeEnum(
    override val code: String,
    override val message: String,
    override val httpStatus: HttpStatus
) : ErrorCode {


    NOT_FOUND(
        "MEMBER_001",
        "해당 사용자를 찾을 수 없습니다.",
        HttpStatus.NOT_FOUND
    ),

    DUPLICATED_EMAIL(
        "MEMBER_002",
        "이미 사용 중인 이메일입니다.",
        HttpStatus.CONFLICT
    )
}
