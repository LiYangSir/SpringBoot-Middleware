package com.quguai.lockredis.dao;

import com.quguai.lockredis.entity.UserReg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRegRepository extends JpaRepository<UserReg, Integer> {

    UserReg findByUsername(String username);
}
