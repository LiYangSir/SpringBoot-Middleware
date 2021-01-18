package com.quguai.redissoninaction.api;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class PraiseRankDto {
    private Integer blogId;
    private Long total;
}
