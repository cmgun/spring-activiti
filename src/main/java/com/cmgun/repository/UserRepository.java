package com.cmgun.repository;

import com.cmgun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chenqilin
 * @Date 2019/7/2
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
}
