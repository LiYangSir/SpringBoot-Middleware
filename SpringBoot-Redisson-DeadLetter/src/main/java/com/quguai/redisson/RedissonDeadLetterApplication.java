package com.quguai.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedissonDeadLetterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedissonDeadLetterApplication.class, args);
	}

}
