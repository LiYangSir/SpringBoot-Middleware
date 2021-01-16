package com.quguai;

import com.quguai.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisCachePenetrationApplicationTests {

	@Resource
	private ItemMapper mapper;
	@Test
	void contextLoads() {
		System.out.println(mapper.selectByCode("book_10010"));
	}

}
