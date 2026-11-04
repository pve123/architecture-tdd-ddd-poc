package io.github.pve123.hexagonal.member.adapter.in.web;

import io.github.pve123.hexagonal.member.application.port.in.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    @Operation(summary = "로그인", description = "받은 accessToken을 Authorization: Bearer 헤더에 담아 보냅니다.")
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        LoginUseCase.Token token = loginUseCase.login(new LoginUseCase.Command(request.email(), request.password()));
        return new TokenResponse("Bearer", token.accessToken(), token.expiresInSeconds());
    }

    record LoginRequest(
            @Schema(example = "user@example.com") @NotBlank String email,
            @Schema(example = "QWERasdf1234!") @NotBlank String password
    ) {
    }

    record TokenResponse(String tokenType, String accessToken, long expiresIn) {
    }
}
