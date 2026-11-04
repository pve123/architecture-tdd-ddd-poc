package io.github.pve123.hexagonal.common.config;

import io.github.pve123.hexagonal.common.error.BusinessException;
import io.github.pve123.hexagonal.common.error.CommonErrorCode;
import io.github.pve123.hexagonal.common.security.JwtAuthenticationFilter;
import io.github.pve123.hexagonal.common.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 누가 어떤 API를 쓸 수 있는지 정합니다. 세션 없이 JWT로만 인증합니다.
 *
 * <ul>
 *   <li>누구나: 회원가입, 로그인, 게시글 읽기, API 문서</li>
 *   <li>로그인한 회원: 내 정보, 게시글 쓰기·고치기·지우기</li>
 *   <li>관리자: /v1/admin/**</li>
 * </ul>
 */
@Configuration
public class SecurityConfig {

    private final HandlerExceptionResolver exceptionResolver;

    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/members", "/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/boards", "/v1/boards/*").permitAll()
                        .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                // 401·403도 GlobalExceptionHandler를 거쳐 다른 오류와 같은 모양으로 응답합니다.
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, e) -> exceptionResolver.resolveException(
                                request, response, null, new BusinessException(CommonErrorCode.UNAUTHORIZED)))
                        .accessDeniedHandler((request, response, e) -> exceptionResolver.resolveException(
                                request, response, null, new BusinessException(CommonErrorCode.FORBIDDEN))))
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
