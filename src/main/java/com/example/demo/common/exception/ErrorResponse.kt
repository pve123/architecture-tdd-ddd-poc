package com.example.demo.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
