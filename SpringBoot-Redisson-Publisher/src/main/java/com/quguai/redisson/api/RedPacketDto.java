package com.quguai.redisson.api;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.omg.CORBA.Object;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class RedPacketDto implements Serializable {
    private Integer usrId;
    private Integer total;
    private Integer amount;
}
