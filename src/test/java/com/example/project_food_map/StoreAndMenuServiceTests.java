package com.example.project_food_map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ProjectFoodMapApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 為了可以使用@BeforeAll和@AfterAll
class StoreAndMenuServiceTests {

	@Test
	void contextLoads() {
	}

}
