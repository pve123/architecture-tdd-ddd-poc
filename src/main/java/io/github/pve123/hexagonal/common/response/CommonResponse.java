package io.github.pve123.hexagonal.common.response;

public record CommonResponse<T>(
        String message,
        T data
) {

}
