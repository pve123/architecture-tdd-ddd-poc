package io.github.pve123.hexagonal.member.application.port.out;

import io.github.pve123.hexagonal.member.application.port.in.LoginUseCase.Token;
import io.github.pve123.hexagonal.member.domain.Role;

/** 토큰을 만드는 출구입니다. 서비스는 JWT라는 사실을 모릅니다. */
public interface TokenIssuerPort {

    Token issue(String memberId, Role role);
}
