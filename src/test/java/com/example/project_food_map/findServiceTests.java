package com.example.project_food_map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ProjectFoodMapApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // ���F�i�H�ϥ�@BeforeAll�M@AfterAll
class findServiceTests {

	@Test
	void contextLoads() {
	}

}
