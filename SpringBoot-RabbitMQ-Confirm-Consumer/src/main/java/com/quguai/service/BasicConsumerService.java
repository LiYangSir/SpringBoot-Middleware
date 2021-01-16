package com.quguai.service;

import com.quguai.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BasicConsumerService {

    @RabbitListener(queues = "middle.mq.basic.info.queue", containerFactory = "simpleListenerContainerAuto")
    public void receiveMsg(Person person) {
        log.info("Receive Msg: " + person);
    }

}
