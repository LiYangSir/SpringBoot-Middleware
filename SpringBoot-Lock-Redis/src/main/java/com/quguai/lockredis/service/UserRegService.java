package com.quguai.lockredis.service;

import com.quguai.lockredis.dao.UserRegRepository;
import com.quguai.lockredis.entity.UserReg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserRegService {

    @Autowired
    private UserRegRepository userRegRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void userRegNoLock(String username, String password) throws Exception {
        UserReg userReg = userRegRepository.findByUsername(username);
        if (userReg == null) {
            log.info("未使用redis分布式锁，当前用户名：{}", username);
            userReg = new UserReg();
            userReg.setUsername(username);
            userReg.setPassword(password);
            userReg.setCreateTime(new Date());
            userRegRepository.save(userReg);
        } else {
            log.info("用户信息已经注册，注册名字：{}", username);
            throw new Exception("用户信息已经注册");
        }
    }
    public void userRegWithLock(String username, String password) throws Exception {
        final String key = username + "-lock";
        final String value = System.nanoTime() + "" + UUID.randomUUID();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Boolean aBoolean = valueOperations.setIfAbsent(key, value);
        if (aBoolean) {
            stringRedisTemplate.expire(key, 20L, TimeUnit.SECONDS);
            try {
                UserReg userReg = userRegRepository.findByUsername(username);
                if (userReg == null) {
                    log.info("使用redis分布式锁，当前用户名：{}", username);
                    userReg = new UserReg();
                    userReg.setUsername(username);
                    userReg.setPassword(password);
                    userReg.setCreateTime(new Date());
                    userRegRepository.save(userReg);
                } else {
                    log.info("用户信息已经注册，注册名字：{}", username);
                    throw new Exception("用户信息已经注册");
                }
            } catch (Exception e) {
                throw e;
            }finally {
                if (value.equals(valueOperations.get(key))) {
                    stringRedisTemplate.delete(key);
                }
            }
        }

    }
}
