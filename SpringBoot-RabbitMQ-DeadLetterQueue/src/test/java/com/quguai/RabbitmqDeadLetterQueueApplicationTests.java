package com.quguai;

import com.quguai.entity.DeadInfo;
import com.quguai.publisher.DeadPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqDeadLetterQueueApplicationTests {

	@Autowired
	private DeadPublisher deadPublisher;

	@Test
	void contextLoads() throws InterruptedException {
		DeadInfo info = new DeadInfo(1, "First");
		deadPublisher.sendMsg(info);
		info = new DeadInfo(2, "First");
		deadPublisher.sendMsg(info);
		Thread.sleep(30000);
	}

}
