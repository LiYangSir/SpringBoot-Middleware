package com.quguai.redisson.controller;

import com.quguai.redisson.api.RedPacketDto;
import com.quguai.redisson.publisher.QueuePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

    @Autowired
    private QueuePublisher queuePublisher;

    @GetMapping("/redisson/queue")
    public RedPacketDto getQueue(RedPacketDto dto) {
        queuePublisher.sendMsg(dto);
        return dto;
    }
}
