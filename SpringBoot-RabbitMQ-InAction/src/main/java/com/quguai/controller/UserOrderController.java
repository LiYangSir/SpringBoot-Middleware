package com.quguai.controller;

import com.quguai.entity.UserOrder;
import com.quguai.entity.UserOrderDto;
import com.quguai.service.DeadUserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class UserOrderController {
    private static final String prefix = "user/order";

    @Autowired
    private DeadUserOrderService deadUserOrderService;

    @PostMapping(value = prefix + "/push")
    public UserOrder login(@RequestBody @Validated UserOrderDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return null;
        }
        return deadUserOrderService.pushUserOrder(dto);
    }
}
