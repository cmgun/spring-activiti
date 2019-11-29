package com.cmgun.mapper;

import com.cmgun.api.model.HistoryQueryRequest;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenqilin
 * @date 2019/7/29
 */
@Mapper
public interface ActHistoryMapper {

    /**
     * 根据参与组查询历史任务
     *
     * @param param    查询参数
     * @return 历史任务
     */
    List<HistoricTaskInstance> queryByCandidateGroup(@Param("param") HistoryQueryRequest param);

    /**
     * 根据参与组查询历史任务
     *
     * @param param 查询参数
     * @param firstRow 分页开始位置
     * @param pageSize 分页大小
     * @return 历史任务
     */
    List<HistoricTaskInstance> queryByCandidateGroupByPage(@Param("param") HistoryQueryRequest param
            , @Param("firstRow") int firstRow
            , @Param("pageSize") int pageSize);

    /**
     * 根据参与组统计符合条件的历史任务数量
     *
     * @param param 查询参数
     * @return 符合条件的数量
     */
    long countByCandidateGroup(@Param("param") HistoryQueryRequest param);

    /**
     * 查询指定任务id的上一个任务节点，按时间排序取最靠近指定id的一条
     *
     * @param processInstanceId
     * @param currentTaskId
     * @return
     */
    HistoricTaskInstance queryLastTask(@Param("processInstanceId") String processInstanceId, @Param("currentTaskId") String currentTaskId);
}
