package com.quguai.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mq_order")
@Data
@ToString
@NoArgsConstructor
public class MqOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date businessTime;

    private String memo;
}
