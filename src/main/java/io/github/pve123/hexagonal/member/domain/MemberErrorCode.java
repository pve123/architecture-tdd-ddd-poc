package io.github.pve123.hexagonal.member.domain;

import io.github.pve123.hexagonal.common.error.ErrorCode;
import io.github.pve123.hexagonal.common.error.ErrorType;

public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("MEMBER_001", "해당 사용자를 찾을수 없습니다.", ErrorType.NOT_FOUND),
    MEMBER_WITHDRAWN("MEMBER_002", "탈퇴한 사용자입니다.", ErrorType.BAD_REQUEST),
    EMAIL_DUPLICATED("MEMBER_003", "이미 가입된 이메일입니다.", ErrorType.CONFLICT),
    LOGIN_FAILED("MEMBER_004", "이메일 또는 비밀번호가 맞지 않습니다.", ErrorType.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final ErrorType type;

    MemberErrorCode(String code, String message, ErrorType type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public ErrorType type() {
        return type;
    }
}
