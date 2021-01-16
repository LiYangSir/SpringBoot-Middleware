package cn.quguai.lockzk.dao;

import cn.quguai.lockzk.entity.UserReg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRegRepository extends JpaRepository<UserReg, Integer> {

    UserReg findByUsername(String username);
}
