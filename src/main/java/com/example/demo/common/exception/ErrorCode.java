package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

public sealed interface ErrorCode permits MemberErrorCodeEnum {

    String code();

    String message();

    HttpStatus httpStatus();
}
