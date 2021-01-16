package com.quguai.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserOrderDto {
    @NotNull
    private String orderNo;

    @NotNull
    private Integer userId;
}
