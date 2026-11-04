package io.github.pve123.hexagonal.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
