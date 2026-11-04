package io.github.pve123.hexagonal.board.domain;

import io.github.pve123.hexagonal.common.error.ErrorCode;
import io.github.pve123.hexagonal.common.error.ErrorType;

public enum BoardErrorCode implements ErrorCode {

    POST_NOT_FOUND("BOARD_001", "해당 게시글을 찾을수 없습니다.", ErrorType.NOT_FOUND),
    POST_AUTHOR_MISMATCH("BOARD_003", "수정 권한 없음", ErrorType.BAD_REQUEST);

    private final String code;
    private final String message;
    private final ErrorType type;

    BoardErrorCode(String code, String message, ErrorType type) {
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
