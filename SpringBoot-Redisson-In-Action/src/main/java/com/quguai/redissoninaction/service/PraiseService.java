package com.quguai.redissoninaction.service;

import com.quguai.redissoninaction.api.PraiseDto;
import com.quguai.redissoninaction.api.PraiseRankDto;
import com.quguai.redissoninaction.dao.PraiseRepository;
import com.quguai.redissoninaction.entity.Praise;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PraiseService {

    public static final String keyAddBlogLock = "RedisBlogPraiseAddLock";

    @Autowired
    private PraiseRepository praiseRepository;

    @Autowired
    @Qualifier("config")
    private RedissonClient redissonClient;

    @Autowired
    private RedisPraise redisPraise;

    @Transactional
    public void addPraiseLock(PraiseDto dto) {
        final String lockName = keyAddBlogLock + dto.getBlogId() + "-" + dto.getUserId();
        RLock lock = redissonClient.getLock(lockName);
        lock.lock(10L, TimeUnit.SECONDS);
        try {
            Praise praise = praiseRepository.findByBlogIdAndUserId(dto.getBlogId(), dto.getUserId());
            if (praise == null) {
                praise = new Praise();
                BeanUtils.copyProperties(dto, praise);
                praise.setPraiseTime(new Date());
                praise.setStatus(1);
                Praise save = praiseRepository.saveAndFlush(praise);
                if (save != null) {
                    log.info("点赞成功：{}", dto.getBlogId());
                    redisPraise.cachePraiseBlog(dto.getBlogId(), dto.getUserId(), 1);
                    this.cachePraiseTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void addPraise(PraiseDto dto) throws InterruptedException {
        Praise praise = praiseRepository.findByBlogIdAndUserId(dto.getBlogId(), dto.getUserId());
        if (praise == null) {
            praise = new Praise();
            BeanUtils.copyProperties(dto, praise);
            praise.setPraiseTime(new Date());
            praise.setStatus(1);
            Praise save = praiseRepository.saveAndFlush(praise);
            if (save != null) {
                log.info("点赞成功：{}", dto.getBlogId());
                redisPraise.cachePraiseBlog(dto.getBlogId(), dto.getUserId(), 1);
                this.cachePraiseTotal();
            }
        }
    }

    @Transactional
    public void cancelPraiseLock(PraiseDto dto) {
        final String lockName = keyAddBlogLock + dto.getBlogId() + "-" + dto.getUserId();
        RLock lock = redissonClient.getLock(lockName);
        lock.lock(10L, TimeUnit.SECONDS);
        try {
            if (dto.getBlogId() != null && dto.getUserId() != null) {
                Integer res = praiseRepository.cancelPraiseBlog(dto.getBlogId(), dto.getUserId());
                if (res > 0) {
                    log.info("取消点赞：{}， 更新点赞成功", dto.getBlogId());
                    redisPraise.cachePraiseBlog(dto.getBlogId(), dto.getUserId(), 0);
                    this.cachePraiseTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void cancelPraise(PraiseDto dto) throws InterruptedException {
        if (dto.getBlogId() != null && dto.getUserId() != null) {
            Integer res = praiseRepository.cancelPraiseBlog(dto.getBlogId(), dto.getUserId());
            if (res > 0) {
                log.info("取消点赞：{}， 更新点赞成功", dto.getBlogId());
                redisPraise.cachePraiseBlog(dto.getBlogId(), dto.getUserId(), 0);
                this.cachePraiseTotal();
            }
        }

    }

    public Long getBlogPraiseTotal(Integer blogId) {
        return redisPraise.getCacheTotalBlog(blogId);
    }

    // 获取排行榜
    public Collection<PraiseRankDto> getRankWithRedisson() {
        return redisPraise.getBlogPraiseRank();
    }

    // 更新排行榜
    private void cachePraiseTotal() {
        redisPraise.rankBlogPraise();
    }
}
