package io.github.pve123.hexagonal.common.error;

public interface ErrorCode {

    String code();

    String message();

    ErrorType type();
}
