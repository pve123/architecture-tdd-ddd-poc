package io.github.pve123.hexagonal.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Authorization 헤더의 토큰을 읽어 "누가 요청했는지"를 기록합니다.
 * 컨트롤러는 {@code @AuthenticationPrincipal String memberId}로 꺼내 씁니다.
 *
 * <p>빈으로 등록하면 서블릿 필터로 한 번 더 걸리므로 SecurityConfig에서 직접 만듭니다.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            tokenProvider.read(header.substring(BEARER.length()))
                    .ifPresent(member -> SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    member.memberId(),
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + member.role())))));
        }
        chain.doFilter(request, response);
    }
}
