package com.quguai.lockaction.dao;

import com.quguai.lockaction.entity.BookRob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookRobRepository extends JpaRepository<BookRob, Integer> {
    Integer countBookRobByBookNoAndUserId(String bookNo, Integer userId);
}
