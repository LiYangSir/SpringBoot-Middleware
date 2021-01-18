package com.quguai.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
class RedissonTestApplicationTests {

	@Qualifier("config")
	@Autowired
	private RedissonClient redissonClient;
	@Test
	void contextLoads() throws IOException {
		System.out.println(redissonClient.getConfig().toJSON());
	}

	@Test
	void useBloomFilter() {
		RBloomFilter<Object> myBloomFilter = redissonClient.getBloomFilter("myBloomFilter");
		myBloomFilter.tryInit(1000L, 0.01);
		for (int i = 0; i < 1000; i++) {
			myBloomFilter.add(i);
		}
		log.info("是否包含-1： {}", myBloomFilter.contains(-1));
		log.info("是否包含1： {}", myBloomFilter.contains(1));
		log.info("是否包含1000： {}", myBloomFilter.contains(100));
		log.info("是否包含1000000： {}", myBloomFilter.contains(10000));
	}

}
