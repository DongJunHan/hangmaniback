package com.project.hangmani;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {
		"file:../hangmani_config/application-local.properties",
		"classpath:application.properties"
})
class HangmaniApplicationTests {

	@Test
	void contextLoads() {
	}

}
