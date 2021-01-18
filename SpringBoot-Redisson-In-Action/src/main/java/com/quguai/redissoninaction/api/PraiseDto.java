package com.quguai.redissoninaction.api;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PraiseDto {
    private Integer blogId;
    private Integer userId;
}
