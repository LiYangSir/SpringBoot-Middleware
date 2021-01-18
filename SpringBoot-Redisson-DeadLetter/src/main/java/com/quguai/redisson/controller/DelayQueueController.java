package com.quguai.redisson.controller;

import com.quguai.redisson.publisher.DelayQueuePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelayQueueController {

    @Autowired
    private DelayQueuePublisher queuePublisher;

    @GetMapping("delay/queue")
    public void quw() {
        queuePublisher.sendMsg("A", 8000L);
        queuePublisher.sendMsg("B", 2000L);
        queuePublisher.sendMsg("C", 5000L);
        queuePublisher.sendMsg("D", 4000L);
    }
}
