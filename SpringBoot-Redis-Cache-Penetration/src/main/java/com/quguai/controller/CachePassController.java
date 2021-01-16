package com.quguai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quguai.entity.Item;
import com.quguai.service.CachePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CachePassController {
    @Autowired
    private CachePassService cachePassService;

    @GetMapping("/item/info")
    public Item getItem(@RequestParam String itemCode) throws JsonProcessingException {
        return cachePassService.getItemInfo(itemCode);
    }
}
