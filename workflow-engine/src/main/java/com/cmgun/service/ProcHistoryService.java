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
     * 查询流程历史任务
     *
     * @param request 参数
     * @return 流程历史任务列表
     */
    PageResult<History> queryHistory(HistoryQueryRequest request);
}
