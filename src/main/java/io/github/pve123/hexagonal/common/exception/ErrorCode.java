package io.github.pve123.hexagonal.common.exception;

import org.springframework.http.HttpStatus;

public sealed interface ErrorCode permits MemberErrorCodeEnum, BoardErrorCodeEnum {

    String code();

    String message();

    HttpStatus httpStatus();
}
