package com.example.demo.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "테스트 전용 API",
                description = "개인적으로 테스트하기 위한 API 문서입니다",
                version = "v1"
        )
)
public class SwaggerConfiguration {
}
