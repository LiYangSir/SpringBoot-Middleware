package com.quguai.lockaction.api;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedPacketDto {
    private Integer usrId;
    @NotNull
    private Integer total;
    @NotNull
    private Integer amount;
}
