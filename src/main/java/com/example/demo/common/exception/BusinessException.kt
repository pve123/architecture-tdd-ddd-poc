package com.example.demo.common.exception

class BusinessException(
    val errorCode: ErrorCode
) : RuntimeException(

    errorCode.message
)
