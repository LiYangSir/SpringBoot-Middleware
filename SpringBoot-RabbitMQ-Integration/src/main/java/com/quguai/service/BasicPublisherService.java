package com.quguai.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicPublisherService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String msg) {
        if (Strings.isNotBlank(msg)) {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.convertAndSend("middle.mq.basic.info.exchange", "middle.mq.basic.info.routing.key", msg);
        }
    }

    public void sendMessage(Object msg) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("middle.mq.basic.info.exchange", "middle.mq.basic.info.routing.key", msg);

    }
}
