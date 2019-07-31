package com.cmgun.mapper;

import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenqilin
 * @Date 2019/7/29
 */
@Mapper
public interface ActHistoryMapper {

    /**
     * 根据参与组查询历史任务
     *
     * @param category 类型
     * @param groupId 参与组id
     * @param firstRow 分页开始位置
     * @param pageSize 分页大小
     * @return 历史任务
     */
    List<HistoricTaskInstance> queryByCandidateGroup(@Param("category") String category
            , @Param("groupId") String groupId
            , @Param("firstRow") int firstRow
            , @Param("pageSize") int pageSize);

    /**
     * 根据参与组统计符合条件的历史任务数量
     *
     * @param category 类型
     * @param groupId 参与组id
     * @return 符合条件的数量
     */
    long countByCandidateGroup(@Param("category") String category, @Param("groupId") String groupId);
}
