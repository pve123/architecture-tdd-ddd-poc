package com.example.demo.common.exception

import jakarta.persistence.EntityNotFoundException
import org.hibernate.TransientPropertyValueException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val ec = e.errorCode
        return ResponseEntity
            .status(ec.httpStatus)
            .body(ErrorResponse.of(ec))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.BAD_REQUEST

        val fieldError = e.bindingResult.fieldError
        val message = if (fieldError != null) {
            val field = fieldError.field
            val msg = fieldError.defaultMessage ?: "유효하지 않은 값입니다."
            "[$field] $msg"
        } else {
            "유효성 검증에 실패했습니다."
        }

        // ✅ code는 일관되게 관리하는 게 베스트.
        // 지금은 기존 구현 유지 느낌으로 httpStatus.name() 사용.
        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse.of(httpStatus.name, message))
    }

    @ExceptionHandler(
        HttpMessageNotReadableException::class,
        SQLIntegrityConstraintViolationException::class,
        TransientPropertyValueException::class
    )
    fun handleBadRequestExceptions(e: Exception): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.BAD_REQUEST

        // 운영에서는 e.message 그대로 노출하지 않는 게 보통 더 안전합니다.
        val message = e.message ?: "잘못된 요청입니다."

        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse.of(httpStatus.name, message))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.NOT_FOUND
        val message = e.message ?: "리소스를 찾을 수 없습니다."

        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse.of(httpStatus.name, message))
    }

    /**
     * ✅ 마지막 fallback: 예상치 못한 예외
     * - 운영에서는 반드시 공통 500으로 내려야 함
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(e: Exception): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        val message = "서버 오류가 발생했습니다."

        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse.of(httpStatus.name, message))
    }
}
