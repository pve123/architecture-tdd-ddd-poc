package io.github.pve123.hexagonal.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * 현재 시각은 이 Clock으로만 얻습니다.
 * 테스트에서 시각을 고정할 수 있어서, 삭제 시각 같은 값을 정확히 검사할 수 있습니다.
 */
@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
