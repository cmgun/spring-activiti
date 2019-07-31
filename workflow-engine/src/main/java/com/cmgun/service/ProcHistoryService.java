package com.cmgun.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.History;
import com.cmgun.api.model.HistoryQueryRequest;

/**
 * 流程历史管理
 *
 * @author chenqilin
 * @Date 2019/7/29
 */
public interface ProcHistoryService {

    /**
     * 查询流程历史任务，支持参数里的所有字段
     *
     * @param request 参数
     * @return 流程历史任务列表
     */
    PageResult<History> queryHistory(HistoryQueryRequest request);

    /**
     * 根据参与组查询历史任务
     * note:
     * 不使用框架查询方式
     * 因为框架自带查询不允许查已经分配了Assignee后再根据Candidate*字段进行查询
     *
     * @param request 参数
     * @return 流程历史任务列表
     */
    PageResult<History> queryHistoryByCandidateGroup(HistoryQueryRequest request);
}
