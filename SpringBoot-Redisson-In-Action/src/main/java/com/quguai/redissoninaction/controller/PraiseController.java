package com.quguai.redissoninaction.controller;

import com.quguai.redissoninaction.api.BaseResponse;
import com.quguai.redissoninaction.api.PraiseDto;
import com.quguai.redissoninaction.api.StatusCode;
import com.quguai.redissoninaction.service.PraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PraiseController {

    @Autowired
    private PraiseService praiseService;

    @GetMapping("/blog/praise/add")
    public BaseResponse<String> addPraise(PraiseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new BaseResponse<>(StatusCode.InvalidParams);
        }
        BaseResponse<String> response = new BaseResponse<>(StatusCode.Success);
        praiseService.addPraiseLock(dto);
        Long total = praiseService.getBlogPraiseTotal(dto.getBlogId());
        response.setData(total.toString());
        return response;
    }

    @GetMapping("/blog/praise/cancel")
    public BaseResponse<String> cancelPraise(PraiseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new BaseResponse<>(StatusCode.InvalidParams);
        }
        BaseResponse<String> response = new BaseResponse<>(StatusCode.Success);
        praiseService.cancelPraiseLock(dto);
        Long total = praiseService.getBlogPraiseTotal(dto.getBlogId());
        response.setData(total.toString());
        return response;
    }

    @GetMapping("/blog/praise/total/rank")
    public BaseResponse<String> rankPraise() {
        BaseResponse<String> response = new BaseResponse<>(StatusCode.Success);
        response.setData(praiseService.getRankWithRedisson().toString());
        return response;
    }
}
