package com.quguai.service;

import com.quguai.dao.MqOrderRepository;
import com.quguai.dao.UserOrderRepository;
import com.quguai.entity.MqOrder;
import com.quguai.entity.UserOrder;
import com.quguai.entity.UserOrderDto;
import com.quguai.publisher.DeadOrderPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class DeadUserOrderService {
    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private MqOrderRepository mqOrderRepository;

    @Autowired
    private DeadOrderPublisher publisher;

    public UserOrder pushUserOrder(UserOrderDto userOrderDto) {
        UserOrder userOrder = new UserOrder();
        BeanUtils.copyProperties(userOrderDto, userOrder);
        userOrder.setStatus(1);
        userOrder.setCreateTime(new Date());
        userOrderRepository.save(userOrder);
        log.info("用户下单成功， 下单信息为{}", userOrder);
        publisher.sendMsg(userOrder.getId());
        return userOrder;
    }

    public void updateUserOrderRecord(UserOrder userOrder) {
        if (userOrder != null) {
            userOrder.setIsActive(0);
            userOrder.setUpdateTime(new Date());
            userOrderRepository.save(userOrder);
            MqOrder mqOrder = new MqOrder();
            mqOrder.setBusinessTime(new Date());
            mqOrder.setMemo("更新失效当前用户下单记录Id, orderId="+ userOrder.getId());
            mqOrder.setOrderId(userOrder.getId());
            mqOrderRepository.save(mqOrder);
        }
    }
}
