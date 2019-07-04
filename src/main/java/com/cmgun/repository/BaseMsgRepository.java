package com.cmgun.repository;

import com.cmgun.entity.BaseMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author chenqilin
 * @Date 2019/7/2
 */
@Repository
public interface BaseMsgRepository extends JpaRepository<BaseMsg, Long> {

    @Modifying
    @Query("update BaseMsg set state = :newState where id = :id and state = :oldState")
    void updateState(@Param("id")Integer id, @Param("newState")Integer newState
            , @Param("oldState")Integer oldState);

    @Query("from BaseMsg where msg = :msg")
    BaseMsg findByMsg(@Param("msg") String msg);
}
