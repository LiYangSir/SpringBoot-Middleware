package com.quguai.controller;

import com.quguai.api.BaseResponse;
import com.quguai.api.RedPacketDto;

import com.quguai.api.StatusCode;
import com.quguai.service.impl.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class RedPacketController {
    @Autowired
    private RedPacketService redPacketService;

    @PostMapping(value = "/hand/out")
    public BaseResponse<String> handout(@Validated @RequestBody RedPacketDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResponse<>(StatusCode.InvalidParams);
        }
        BaseResponse<String> response = new BaseResponse<>(StatusCode.Success);
        String redId = redPacketService.handout(dto);
        response.setData(redId);
        return response;
    }

    @GetMapping("/rob")
    public BaseResponse<String> rob(@RequestParam Integer userId, @RequestParam String redId) {
        BigDecimal result = redPacketService.rob(userId, redId);
        BaseResponse<String> response = new BaseResponse<>(StatusCode.Success);
        if (result != null)
            response.setData(result.toString());
        else
            response = new BaseResponse<String>(StatusCode.Fail.getCode(), "红包已经抢完");
        return response;
    }
}
