package com.quguai.consumer;

import com.quguai.dao.UserOrderRepository;
import com.quguai.entity.UserOrder;
import com.quguai.service.DeadUserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadOrderConsumer {
    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private DeadUserOrderService deadUserOrderService;

    @RabbitListener(queues = "mq.consumer.real.queue.name")
    public void consumerMsg(Integer orderId) {
        log.info("用户下单支付超时， 监听内容：{}", orderId);
        UserOrder userOrder = userOrderRepository.findByIdAndStatus(orderId, 1);
        if (userOrder != null) {
            deadUserOrderService.updateUserOrderRecord(userOrder);
        }
    }
}
