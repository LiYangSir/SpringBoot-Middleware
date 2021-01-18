package com.quguai.redisson;

import com.quguai.redisson.api.RedPacketDto;
import com.quguai.redisson.publiser.UserLogPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class RedissonPublisherApplicationTests {

	@Autowired
	private UserLogPublisher userLogPublisher;

	@Qualifier("config")
	@Autowired
	private RedissonClient redissonClient;

	@Test
	void contextLoads() throws InterruptedException {
		userLogPublisher.sendMsg(new RedPacketDto(1, 100, 1000));
		Thread.sleep(1000);
	}

	@Test
	void rmap() {
		RMap<Integer, RedPacketDto> redissonMap = redissonClient.getMap("redissonMap");
		RedPacketDto dto1 = new RedPacketDto(1, 100, 1000);
		RedPacketDto dto2 = new RedPacketDto(2, 100, 1000);
		RedPacketDto dto3 = new RedPacketDto(3, 100, 1000);
		RedPacketDto dto4 = new RedPacketDto(4, 100, 1000);

		redissonMap.put(dto1.getUsrId(), dto1);
		redissonMap.putAsync(dto1.getUsrId(), dto1);

		redissonMap.putIfAbsent(dto2.getUsrId(), dto2);
		redissonMap.putIfAbsentAsync(dto2.getUsrId(), dto2);

		redissonMap.fastPut(dto3.getUsrId(), dto3);
		redissonMap.fastPutAsync(dto3.getUsrId(), dto3);

		redissonMap.fastPutIfAbsent(dto4.getUsrId(), dto4);
		redissonMap.fastPutIfAbsentAsync(dto4.getUsrId(), dto4);
	}

	@Test
	void getMap() {
		RMap<Integer, RedPacketDto> redissonMap = redissonClient.getMap("redissonMap");
		Set<Integer> ids = redissonMap.keySet();
		Map<Integer, RedPacketDto> all = redissonMap.getAll(ids);
		log.info("all : {} ", all);

		redissonMap.remove(6);
		all = redissonMap.getAll(redissonMap.keySet());
		log.info("remove: {}", all);

		redissonMap.fastRemove(1, 2, 3);
		all = redissonMap.getAll(redissonMap.keySet());
		log.info("removeSome: {}", all);
	}

	@Test  // 元素淘汰
	void deparate() {
		RMapCache<Integer, RedPacketDto> redissonMap = redissonClient.getMapCache("redissonMapCache");
		RedPacketDto dto1 = new RedPacketDto(1, 100, 1000);
		RedPacketDto dto2 = new RedPacketDto(2, 100, 1000);
		RedPacketDto dto3 = new RedPacketDto(3, 100, 1000);
		RedPacketDto dto4 = new RedPacketDto(4, 100, 1000);

		redissonMap.put(dto1.getUsrId(), dto1);
		redissonMap.put(dto1.getUsrId(), dto1, 10L, TimeUnit.HOURS);
		redissonMap.putAsync(dto1.getUsrId(), dto1);

		redissonMap.putIfAbsent(dto2.getUsrId(), dto2);
		redissonMap.putIfAbsent(dto2.getUsrId(), dto2, 10L, TimeUnit.SECONDS);
		redissonMap.putIfAbsentAsync(dto2.getUsrId(), dto2);
	}

	@Test
	void setTest() {
		RSortedSet<RedPacketDto> redissonSet = redissonClient.getSortedSet("redissonSet");
		// 增加自定义比较器
		redissonSet.trySetComparator(new RSetComparator());
		RedPacketDto dto1 = new RedPacketDto(1, 100, 1000);
		RedPacketDto dto2 = new RedPacketDto(2, 100, 1000);
		RedPacketDto dto3 = new RedPacketDto(3, 100, 1000);
		RedPacketDto dto4 = new RedPacketDto(4, 100, 1000);

		redissonSet.add(dto1);
		redissonSet.addAsync(dto2);
		Collection<RedPacketDto> redPacketDtos = redissonSet.readAll();
		System.out.println(redPacketDtos);
	}

	private class RSetComparator implements Comparator<RedPacketDto>{
		@Override
		public int compare(RedPacketDto o1, RedPacketDto o2) {
			return o2.getUsrId() - o1.getUsrId();
		}
		@Override
		public Comparator<RedPacketDto> reversed() {
			return null;
		}
	}

	@Test
	void setScore() {
		RScoredSortedSet<Object> redissonScoreSet = redissonClient.getScoredSortedSet("redissonScoreSet");

		RedPacketDto dto1 = new RedPacketDto(1, 100, 1000);
		RedPacketDto dto2 = new RedPacketDto(2, 100, 1000);
		RedPacketDto dto3 = new RedPacketDto(3, 100, 1000);
		RedPacketDto dto4 = new RedPacketDto(4, 100, 1000);

		redissonScoreSet.add(dto1.getUsrId(), dto1);

		Set<Object> objects = redissonScoreSet.readSortAlpha(SortOrder.DESC);
		System.out.println(objects);

		log.info("{}{}", dto1, redissonScoreSet.revRank(dto1) + 1);
		log.info("{}{}", dto1, redissonScoreSet.getScore(dto1));
	}

}
