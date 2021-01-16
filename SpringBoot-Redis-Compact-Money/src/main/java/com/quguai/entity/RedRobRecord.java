package com.quguai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "red_rob_record")
@ToString
public class RedRobRecord {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;
    private String redPacket;
    private BigDecimal amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date robTime;
    private Boolean isActive = false;

    public RedRobRecord(Integer userId, String redPacket, BigDecimal amount, Date robTime) {
        this.userId = userId;
        this.redPacket = redPacket;
        this.amount = amount;
        this.robTime = robTime;
    }
}
