package io.github.pve123.hexagonal.member.application.port.in;

public interface LoginUseCase {

    /** 이메일과 비밀번호가 맞으면 액세스 토큰을 줍니다. 틀리면 LOGIN_FAILED 예외를 던집니다. */
    Token login(Command command);

    record Command(String email, String rawPassword) {
    }

    record Token(String accessToken, long expiresInSeconds) {
    }
}
