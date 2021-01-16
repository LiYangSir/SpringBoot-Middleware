package com.quguai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "red_record")
@NoArgsConstructor
@ToString
public class RedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer usrId;
    private String redPacket;
    private Integer total;
    private BigDecimal amount;
    private Boolean isActive = false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @OneToMany(mappedBy = "redRecord", cascade = {CascadeType.PERSIST})
    private List<RedDetail> redDetails = new ArrayList<>();

    public RedRecord(Integer usrId, String redPacket, Integer total, BigDecimal amount, Date createTime) {
        this.usrId = usrId;
        this.redPacket = redPacket;
        this.total = total;
        this.amount = amount;
        this.createTime = createTime;
    }
}
