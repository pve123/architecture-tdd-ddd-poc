package com.example.demo.config.test;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
public abstract class TestContainerConfig {

    @Container
    protected static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:latest")
            .withDatabaseName(TestDBConfig.DB_NAME)
            .withUsername(TestDBConfig.USERNAME)
            .withPassword(TestDBConfig.PASSWORD);

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
    }
}
