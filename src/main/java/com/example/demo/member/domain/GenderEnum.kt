package com.example.demo.member.domain

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "성별 Enum")
enum class Gender(
    val description: String
) {
    FEMALE("여성"),
    MALE("남성")
}
