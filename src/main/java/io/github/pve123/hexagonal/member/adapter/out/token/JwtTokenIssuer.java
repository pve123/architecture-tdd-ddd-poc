package io.github.pve123.hexagonal.member.adapter.out.token;

import io.github.pve123.hexagonal.common.security.JwtTokenProvider;
import io.github.pve123.hexagonal.member.application.port.in.LoginUseCase.Token;
import io.github.pve123.hexagonal.member.application.port.out.TokenIssuerPort;
import io.github.pve123.hexagonal.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 토큰 출구 포트를 JWT로 구현합니다. 다른 방식으로 바꾸려면 이 클래스만 바꾸면 됩니다. */
@Component
@RequiredArgsConstructor
class JwtTokenIssuer implements TokenIssuerPort {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Token issue(String memberId, Role role) {
        return new Token(jwtTokenProvider.create(memberId, role.name()), jwtTokenProvider.validity().toSeconds());
    }
}
