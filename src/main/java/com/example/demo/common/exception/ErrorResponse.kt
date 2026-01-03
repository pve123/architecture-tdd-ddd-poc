package com.example.demo.common.exception

data class ErrorResponse(
    val code: String,
    val message: String
) {
    companion object {
        fun of(errorCode: ErrorCode) = ErrorResponse(
            code = errorCode.code,
            message = errorCode.message
        )

        fun of(code: String, message: String) = ErrorResponse(code, message)
    }
}