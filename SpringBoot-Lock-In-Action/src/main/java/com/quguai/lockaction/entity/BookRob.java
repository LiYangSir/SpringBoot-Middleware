package com.quguai.lockaction.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "book_rob")
@ToString
public class BookRob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String bookNo;

    @Temporal(TemporalType.DATE)
    private Date robTime;

}
