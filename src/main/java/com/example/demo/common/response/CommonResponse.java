package com.example.demo.common.response;

public record CommonResponse<T>(
        String message,
        T data
) {

}
