package cn.quguai.lockzk.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user_reg")
@ToString
public class UserReg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
}
