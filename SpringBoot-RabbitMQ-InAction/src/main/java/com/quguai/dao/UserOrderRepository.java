package com.quguai.dao;

import com.quguai.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Integer> {
    UserOrder findByIdAndStatus(Integer id, Integer status);
}
