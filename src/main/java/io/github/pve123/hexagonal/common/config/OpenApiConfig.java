package io.github.pve123.hexagonal.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "architecture-tdd-ddd-poc API",
        description = "헥사고날 아키텍처로 만든 회원과 게시글 API",
        version = "v1"))
public class OpenApiConfig {
}
