package com.quguai;

import com.quguai.entity.Person;
import com.quguai.service.BasicPublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqTestApplicationTests {

	@Autowired
	private BasicPublisherService service;


	@Test
	void contextLoads() {
		service.sendMsg("Hello SpringBoot RabbitMQ Middleware");
	}

	@Test
	void test2() {
		Person person = new Person(1, "aaa", "bbb");
		service.sendMessage(person);
	}

}
