package com.quguai.lockaction.service;

import com.quguai.lockaction.dao.BookRobRepository;
import com.quguai.lockaction.dao.BookStockRepository;
import com.quguai.lockaction.entity.BookRob;
import com.quguai.lockaction.entity.BookStock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookRobService {

    @Autowired
    private BookRobRepository bookRobRepository;

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private CuratorFramework client;

    private static final String pathPrefix = "/middleware/zkLock";

    @Transactional(rollbackFor = Exception.class)
    public void robWithNoLock(Integer userId, String bookNo) throws Exception {
        BookStock bookStock = bookStockRepository.findByBookNo(bookNo);
        Integer total = bookRobRepository.countBookRobByBookNoAndUserId(bookNo, userId);
        if (bookStock != null && bookStock.getStock() > 0 && total <= 0) {
            Integer res = bookStockRepository.updateStock(bookNo);
            if (res > 0) {
                BookRob bookRob = new BookRob();
                bookRob.setBookNo(bookNo);
                bookRob.setUserId(userId);
                bookRob.setRobTime(new Date());
                bookRobRepository.save(bookRob);
            }
        } else {
            throw new Exception("库存不足");
        }

    }

    public void robWithZKLock(Integer userId, String bookNo) throws Exception {
        final String key = userId + "-lock";
        InterProcessMutex mutex = new InterProcessMutex(client, pathPrefix + key);
        try {
            if (mutex.acquire(15L, TimeUnit.SECONDS)) {
                BookStock bookStock = bookStockRepository.findByBookNo(bookNo);
                Integer total = bookRobRepository.countBookRobByBookNoAndUserId(bookNo, userId);
                if (bookStock != null && bookStock.getStock() > 0 && total <= 0) {
                    Integer res = bookStockRepository.updateStock(bookNo);
                    if (res > 0) {
                        BookRob bookRob = new BookRob();
                        bookRob.setBookNo(bookNo);
                        bookRob.setUserId(userId);
                        bookRob.setRobTime(new Date());
                        bookRobRepository.saveAndFlush(bookRob);  // 存在事务的问题，所以要注意事务当中只放update/del

                        log.info("抢购成功{}，{}，{}", userId, bookNo, bookRobRepository.countBookRobByBookNoAndUserId(bookNo, userId));
                    }
                } else {
                    throw new Exception("库存不足");
                }
            } else {
                throw new RuntimeException("获取锁失败");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            mutex.release();
        }
    }

}
