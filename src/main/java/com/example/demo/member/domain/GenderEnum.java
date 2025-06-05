package com.example.demo.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "성별 Enum")
public enum GenderEnum {

    FEMALE("여성"),
    MALE("남성");

    private final String description;

}
