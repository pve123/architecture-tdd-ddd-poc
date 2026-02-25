package com.example.demo.common.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum BoardErrorCodeEnum implements ErrorCode {

    POST_NOT_FOUND("BOARD_001", "해당 게시글을 찾을수 없습니다.", HttpStatus.NOT_FOUND),
    POST_WITHDRAWN("BOARD_002", "삭제된 게시글입니다.", HttpStatus.BAD_REQUEST),
    POST_AUTHOR_MISMATCH("BOARD_003", "수정 권한 없음", HttpStatus.BAD_REQUEST);
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
