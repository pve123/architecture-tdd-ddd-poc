package io.github.pve123.hexagonal.common.error;

public enum CommonErrorCode implements ErrorCode {

    UNAUTHORIZED("COMMON_401", "로그인이 필요합니다.", ErrorType.UNAUTHORIZED),
    FORBIDDEN("COMMON_403", "권한이 없습니다.", ErrorType.FORBIDDEN);

    private final String code;
    private final String message;
    private final ErrorType type;

    CommonErrorCode(String code, String message, ErrorType type) {
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
