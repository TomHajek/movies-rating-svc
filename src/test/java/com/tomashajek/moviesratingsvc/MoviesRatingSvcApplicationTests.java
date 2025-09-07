package com.tomashajek.moviesratingsvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MoviesRatingSvcApplicationTests extends TestContainer {

	@Test
	void contextLoads() {
        log.info("Loading Spring context with PostgreSQL Testcontainer!");
	}

}
