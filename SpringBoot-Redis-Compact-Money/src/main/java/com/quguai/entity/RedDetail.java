package com.quguai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "red_detail")
public class RedDetail {

    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal amount;
    private Boolean isActive = false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private RedRecord redRecord;

    public RedDetail(BigDecimal amount, Date createTime, RedRecord redRecord) {
        this.amount = amount;
        this.createTime = createTime;
        this.redRecord = redRecord;
    }
}
