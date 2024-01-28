package com.example.daycarat;

import com.example.daycarat.domain.activity.service.ActivityTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DayCaratApplicationTests {

	@Autowired
	ActivityTagService activityTagService;

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		activityTagService.insertActivityTagSearch(4L, "이건 CMC");
	}

}
