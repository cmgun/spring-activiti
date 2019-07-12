package com.cmgun.repository;

import com.cmgun.entity.BaseMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author chenqilin
 * @Date 2019/7/2
 */
@Repository
public interface BaseMsgRepository extends JpaRepository<BaseMsg, Long> {

    @Modifying
    @Query("update BaseMsg set state = :newState where id = :id and state = :oldState")
    void updateState(@Param("id") Integer id, @Param("newState") Integer newState
            , @Param("oldState") Integer oldState);

    @Query("from BaseMsg where msg = :msg")
    BaseMsg findByMsg(@Param("msg") String msg);

    @Modifying
    @Query("update BaseMsg set update_time = :updateTime where id = :id")
    int updateTime(@Param("id") Integer id, @Param("updateTime") Date updateTime);
}
