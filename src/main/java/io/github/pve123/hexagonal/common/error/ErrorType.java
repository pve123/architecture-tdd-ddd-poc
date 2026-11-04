package io.github.pve123.hexagonal.common.error;

/**
 * 오류의 종류입니다. HTTP 상태 코드로 바꾸는 일은 웹 어댑터가 맡습니다.
 * 그래서 도메인은 HTTP를 몰라도 됩니다.
 */
public enum ErrorType {
    BAD_REQUEST,
    UNAUTHORIZED,
    NOT_FOUND,
    CONFLICT,
    FORBIDDEN
}
