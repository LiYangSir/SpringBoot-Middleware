package com.quguai;

import com.quguai.entity.Person;
import com.quguai.service.KnowledgeManualPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqAutoConfirmConsumerApplicationTests {

	@Autowired
	private KnowledgeManualPublisher service;


	@Test
	void cont() {
		service.sendMessage(new Person(1, "aaa", "aaaadd"));
	}
}
