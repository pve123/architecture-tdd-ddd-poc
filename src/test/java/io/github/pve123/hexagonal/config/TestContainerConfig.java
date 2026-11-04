package io.github.pve123.hexagonal.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.mariadb.MariaDBContainer;

/**
 * 테스트용 MariaDB를 도커로 띄웁니다.
 *
 * <p>테스트 클래스마다 컨테이너를 새로 띄우면, 스프링이 재사용하는 컨텍스트가 이미 꺼진 DB를 가리키게 됩니다.
 * 그래서 JVM에서 한 번만 띄우고 모든 테스트가 함께 씁니다. 접속 정보는 @ServiceConnection이 넣어 줍니다.
 */
@ActiveProfiles("test")
public abstract class TestContainerConfig {

    @ServiceConnection
    protected static final MariaDBContainer mariaDBContainer = new MariaDBContainer("mariadb:11.4");

    static {
        mariaDBContainer.start();
    }
}
