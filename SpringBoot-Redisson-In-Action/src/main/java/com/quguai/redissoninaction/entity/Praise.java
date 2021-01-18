package com.quguai.redissoninaction.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "middleware_praise")
public class Praise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer blogId;
    private Integer userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date praiseTime;

    private Integer status;
    private Integer isActive;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
