package io.github.pve123.hexagonal.common.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MemberErrorCodeEnum implements ErrorCode {

    MEMBER_NOT_FOUND("MEMBER_001", "해당 사용자를 찾을수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_WITHDRAWN("MEMBER_002", "탈퇴한 사용자입니다.", HttpStatus.BAD_REQUEST),
    EMAIL_DUPLICATED("MEMBER_003", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;


    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
