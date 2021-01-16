package com.quguai.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class KnowledgeManualConsumer implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        byte[] msg = message.getBody();
        try {
            channel.basicAck(deliveryTag, true);
            log.info("消息确认消费成功" + message + String.valueOf(msg));
        } catch (Exception e) {
            log.info("发生异常" + e);
            channel.basicReject(deliveryTag, false);
        }
    }
}
