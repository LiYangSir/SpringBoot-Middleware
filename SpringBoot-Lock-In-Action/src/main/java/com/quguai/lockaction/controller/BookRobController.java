package com.quguai.lockaction.controller;

import com.quguai.lockaction.api.BaseResponse;
import com.quguai.lockaction.api.StatusCode;
import com.quguai.lockaction.entity.BookRob;
import com.quguai.lockaction.service.BookRobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BookRobController {

    @Autowired
    private BookRobService bookRobService;

    private static final String prefix = "book/rob";

    @GetMapping(prefix + "/request")
    public BaseResponse<String> takeMoney(Integer userId, String bookNo) {
        if (!StringUtils.hasLength(bookNo) || userId == null || userId < 0) {
            return new BaseResponse<>(StatusCode.InvalidParams);
        }
        BaseResponse<String> baseResponse = new BaseResponse<>(StatusCode.Success);
        try {
//            bookRobService.robWithNoLock(userId, bookNo);
            bookRobService.robWithZKLock(userId, bookNo);
        } catch (Exception e) {
            baseResponse = new BaseResponse<>(StatusCode.Fail.getCode(), e.getMessage());
        }
        return baseResponse;
    }
}
