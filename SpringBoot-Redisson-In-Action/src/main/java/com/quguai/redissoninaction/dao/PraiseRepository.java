package com.quguai.redissoninaction.dao;

import com.quguai.redissoninaction.api.PraiseRankDto;
import com.quguai.redissoninaction.entity.Praise;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PraiseRepository extends JpaRepository<Praise, Integer> {
    Praise findByBlogIdAndUserId(Integer blogId, Integer userId);

    @Query("update Praise set status=0 where blogId = ?1 and userId = ?2")
    @Modifying
    Integer cancelPraiseBlog(Integer blogId, Integer userId);

    @Query(value = "select blog_id AS blogId, count(id) AS total from middleware_redission where is_active=1 and status=1 group by blog_id order by total DESC", nativeQuery = true)
    List<PraiseRankDto> getPraiseRank();
}
