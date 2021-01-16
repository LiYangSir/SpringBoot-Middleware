package com.quguai.dao;

import com.quguai.entity.MqOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MqOrderRepository extends JpaRepository<MqOrder, Integer> {
}
