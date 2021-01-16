package com.quguai.lockaction.dao;

import com.quguai.lockaction.entity.BookStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface BookStockRepository extends JpaRepository<BookStock, Integer> {
    BookStock findByBookNo(String bookNo);

    @Transactional
    @Query(value = "update BookStock set stock = stock - 1 where bookNo = ?1")
    @Modifying
    Integer updateStock(String bookNo);
}
