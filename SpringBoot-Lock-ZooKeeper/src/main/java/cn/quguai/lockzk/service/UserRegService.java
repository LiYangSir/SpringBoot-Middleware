package cn.quguai.lockzk.service;


import cn.quguai.lockzk.dao.UserRegRepository;
import cn.quguai.lockzk.entity.UserReg;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserRegService {

    @Autowired
    private UserRegRepository userRegRepository;

    @Autowired
    private CuratorFramework client;

    private static final String pathPrefix = "/middleware/zkLock";


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
//            log.info("用户信息已经注册，注册名字：{}", username);
            throw new Exception("用户信息已经注册");
        }
    }

    public void userRegWithZKLock(String username, String password) throws Exception {
        final String key = username + "-lock";
        InterProcessMutex mutex = new InterProcessMutex(client, pathPrefix + key);
        try {
            if (mutex.acquire(10L, TimeUnit.SECONDS)) {
                UserReg userReg = userRegRepository.findByUsername(username);
                if (userReg == null) {
                    log.info("使用redis分布式锁，当前用户名：{}", username);
                    userReg = new UserReg();
                    userReg.setUsername(username);
                    userReg.setPassword(password);
                    userReg.setCreateTime(new Date());
                    userRegRepository.save(userReg);
                } else {
//                    log.info("用户信息已经注册，注册名字：{}", username);
                    throw new Exception("用户信息已经注册");
                }
            }else {
                log.info("获取锁失败");
                throw new RuntimeException("获取zk锁失败");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            mutex.release();
        }
    }
}
