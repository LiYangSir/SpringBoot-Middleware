package com.quguai.redisson.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class RedPacketDto implements Serializable {
    private Integer usrId;
    private Integer total;
    private Integer amount;
}
