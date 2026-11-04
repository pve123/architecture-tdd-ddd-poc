package io.github.pve123.hexagonal.common.web;

import io.github.pve123.hexagonal.common.error.BusinessException;
import io.github.pve123.hexagonal.common.error.ErrorCode;
import io.github.pve123.hexagonal.common.error.ErrorType;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예외를 HTTP 응답으로 바꿉니다.
 *
 * <p>예외 메시지에는 SQL 문이나 내부 클래스 이름이 들어 있을 수 있어서 응답에 그대로 담지 않습니다.
 * 바깥에 보여 줄 문구는 ErrorCode나 이 클래스에서 정합니다.
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.errorCode();
        return ResponseEntity
                .status(toHttpStatus(errorCode.type()))
                .body(new ErrorResponse(errorCode.code(), errorCode.message()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null
                ? "입력값이 올바르지 않습니다."
                : "[%s] %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
        return badRequest(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return badRequest("요청 본문을 읽을 수 없습니다. JSON 형식과 값의 종류를 확인해 주세요.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.name(), "저장할 수 없는 값입니다. 이미 있는 값인지 확인해 주세요."));
    }

    private static HttpStatus toHttpStatus(ErrorType type) {
        return switch (type) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
        };
    }

    private static ResponseEntity<ErrorResponse> badRequest(String message) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponse(status.name(), message));
    }
}
