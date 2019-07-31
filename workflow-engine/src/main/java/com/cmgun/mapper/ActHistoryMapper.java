package com.cmgun.mapper;

import com.cmgun.api.model.HistoryQueryRequest;
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
     * @param param 查询参数
     * @param firstRow 分页开始位置
     * @param pageSize 分页大小
     * @return 历史任务
     */
    List<HistoricTaskInstance> queryByCandidateGroup(@Param("param") HistoryQueryRequest param
            , @Param("firstRow") int firstRow
            , @Param("pageSize") int pageSize);

    /**
     * 根据参与组统计符合条件的历史任务数量
     *
     * @param param 查询参数
     * @return 符合条件的数量
     */
    long countByCandidateGroup(@Param("param") HistoryQueryRequest param);
}
