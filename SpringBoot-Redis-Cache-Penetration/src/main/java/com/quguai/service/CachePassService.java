package com.quguai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quguai.entity.Item;
import com.quguai.mapper.ItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CachePassService {
    @Resource
    private ItemMapper itemMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public Item getItemInfo(String itemCode) throws JsonProcessingException {
        Item item = null;
        String key = "item:" + itemCode;
        ValueOperations valueOperations = redisTemplate.opsForValue();

        if (redisTemplate.hasKey(key)) {
            Object res = valueOperations.get(key);
            if (res != null && !Strings.isEmpty(res.toString())) {
                item = objectMapper.readValue(res.toString(), Item.class);
            }
            log.info("缓存查找");
        } else {
            Item item1 = itemMapper.selectByCode(itemCode);
            if (item1 != null) {
                valueOperations.set(key, objectMapper.writeValueAsString(item1));
                log.info("数据库查找");
            } else {
                valueOperations.set(key, "", 30, TimeUnit.MINUTES);
                log.info("缓存击穿");
            }
        }
        return item;
    }
}
