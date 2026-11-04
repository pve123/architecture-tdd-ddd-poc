package io.github.pve123.hexagonal.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/** JWT를 만들고 읽습니다. 토큰에는 회원 ID와 권한만 담습니다. */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenProvider {

    private static final String ROLE = "role";

    private final SecretKey key;
    private final Duration validity;

    public JwtTokenProvider(JwtProperties properties) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
        this.validity = properties.accessTokenValidity();
    }

    public String create(String memberId, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(memberId)
                .claim(ROLE, role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(validity)))
                .signWith(key)
                .compact();
    }

    /** 서명이 틀렸거나 만료된 토큰이면 비어 있는 값을 돌려줍니다. */
    public Optional<LoginMember> read(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return Optional.of(new LoginMember(claims.getSubject(), claims.get(ROLE, String.class)));
        } catch (JwtException | IllegalArgumentException invalidToken) {
            return Optional.empty();
        }
    }

    public Duration validity() {
        return validity;
    }

    public record LoginMember(String memberId, String role) {
    }
}
