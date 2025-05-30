package com.example.demo.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderEnum {

    FEMALE("여성"),
    MALE("남성");

    private final String description;

}
