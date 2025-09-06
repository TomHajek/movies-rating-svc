package com.tomashajek.moviesratingsvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest
class MoviesRatingSvcApplicationTests {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16").withReuse(true);

    // Inject container properties into Spring Boot
    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

	@Test
	void contextLoads() {
        log.info("Loading Spring context with Testcontainers PostgreSQL!");
	}

}
