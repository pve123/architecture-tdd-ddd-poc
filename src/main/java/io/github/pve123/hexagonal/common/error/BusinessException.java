package io.github.pve123.hexagonal.common.error;

/** 규칙을 어겨서 요청을 처리할 수 없을 때 던집니다. */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
