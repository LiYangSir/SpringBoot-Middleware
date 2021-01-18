package com.quguai.redissoninaction.service;

import com.quguai.redissoninaction.api.PraiseDto;
import com.quguai.redissoninaction.api.PraiseRankDto;
import com.quguai.redissoninaction.dao.PraiseRepository;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisPraise {

    public static final String keyBlog = "RedisBlogPraiseMap";

    @Autowired
    @Qualifier("config")
    private RedissonClient redissonClient;

    @Autowired
    private PraiseRepository praiseRepository;

    public void cachePraiseBlog(Integer blogId, Integer uid, Integer status) throws InterruptedException {
        final String lockName = "blogRedissonPraiseLock" + blogId + uid + status;
        RLock lock = redissonClient.getLock(lockName);
        if (lock.tryLock(100, 10, TimeUnit.SECONDS)) {
            if (blogId != null && uid != null && status != null) {
                final String key = blogId + ":" + uid;
                RMap<String, Integer> map = redissonClient.getMap(keyBlog);
                if (status == 1) {
                    map.putIfAbsent(key, uid);
                } else if (status == 0) {
                    map.remove(key);
                }
            }
        }
        lock.forceUnlock();
    }

    public Long getCacheTotalBlog(Integer blogId) {
        Long result = 0L;
        if (blogId != null) {
            RMap<String, Integer> map = redissonClient.getMap(keyBlog);
            Map<String, Integer> allMap = map.readAllMap();
            Set<String> allMapKeySet = allMap.keySet();
            if (map != null && allMapKeySet != null) {
                for (String s : allMapKeySet) {
                    if (StringUtils.hasLength(s)) {
                        String[] split = s.split(":");
                        if (split != null && split.length > 0) {
                            if (blogId.equals(Integer.valueOf(split[0])))
                                result++;
                        }
                    }
                }
            }
        }
        return result;
    }

    public Collection<PraiseRankDto> getBlogPraiseRank() {
        final String key = "praiseRankListKey";
        RList<PraiseRankDto> list = redissonClient.getList(key);
        return list.readAll();
    }

    public void rankBlogPraise() {
        final String key = "praiseRankListKey";
        List<PraiseRankDto> praiseRank = praiseRepository.getPraiseRank();
        if (praiseRank != null && praiseRank.size() > 0) {
            RList<PraiseRankDto> list = redissonClient.getList(key);
            list.clear();
            list.addAll(praiseRank);
        }
    }
}
