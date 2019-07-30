package com.cmgun.service.impl;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.History;
import com.cmgun.api.model.HistoryQueryRequest;
import com.cmgun.service.ProcHistoryService;
import com.cmgun.utils.ExceptionUtil;
import com.cmgun.utils.TaskUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.task.Comment;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程历史任务
 *
 * @author chenqilin
 * @Date 2019/7/29
 */
@Slf4j
@Service
public class ProcHistoryServiceImpl implements ProcHistoryService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;

    @Override
    public PageResult<History> queryHistory(HistoryQueryRequest request) {
        // 任务候选组，非空时忽略其他查询条件
        if (StringUtils.isNotBlank(request.getAssignee())) {
            // 根据任务候选组查询，重写查询语句 TODO 直接返回结果
        }
        // 其余情况
        HistoricTaskInstanceQuery query = getHistoryQuery(request);
        // 总数
        long count = query.count();
        // 分页查询
        List<HistoricTaskInstance> historicTasks = query.listPage(request.getStartRow(), request.getPageSize());
        if (CollectionUtils.isEmpty(historicTasks)) {
            return PageResult.empty(request, count);
        }
        // 结果填充
        List<History> result = Lists.newArrayList();
        for (HistoricTaskInstance historicTaskInstance : historicTasks) {
            History history = getHistory(historicTaskInstance);
            if (history != null) {
                result.add(history);
            }
        }
        return PageResult.result(request, count, result);
    }

    private HistoricTaskInstanceQuery getHistoryQuery(HistoryQueryRequest request) {
        // 任务用户信息不能全为空
        ExceptionUtil.businessException(request.getAssignee() == null && request.getCandidateUser() == null
                , "用户组或用户查询条件不能都为空");
        // 按任务结束时间倒序查询
        HistoricTaskInstanceQuery historyQuery = historyService.createHistoricTaskInstanceQuery()
                .taskCategory(request.getCategory())
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        // 业务key
        if (request.getBusinessKey() != null) {
            historyQuery.processInstanceBusinessKey(request.getBusinessKey());
        }
        // 任务分配人
        if (request.getAssignee() != null) {
            historyQuery.taskAssignee(request.getAssignee());
        }
        // 任务分配用户
        if (request.getCandidateUser() != null) {
            historyQuery.taskCandidateUser(request.getCandidateUser());
        }
        // 是否查询结束任务
        if (BooleanUtils.isTrue(request.getCompleteTask())) {
            historyQuery.finished();
        }
        return historyQuery;
    }

    /**
     * 获取历史任务信息
     *
     * @param historicTaskInstance 历史任务原生实例
     * @return 历史任务信息
     */
    private History getHistory(HistoricTaskInstance historicTaskInstance) {
        // 对应流程
        List<HistoricProcessInstance> processList = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(historicTaskInstance.getProcessInstanceId())
                .list();
        if (CollectionUtils.isEmpty(processList)) {
            log.warn("流程实例不存在，任务id:{}", historicTaskInstance.getId());
            return null;
        }
        // 审批备注
        List<Comment> comments = taskService.getTaskComments(historicTaskInstance.getId());
        // 查询流程参数
        List<HistoricVariableInstance> globalVariables = historyService.createHistoricVariableInstanceQuery()
                .excludeTaskVariables()
                .processInstanceId(historicTaskInstance.getProcessInstanceId())
                .list();
        // 查询任务审批参数
        List<HistoricVariableInstance> localVariables = historyService.createHistoricVariableInstanceQuery()
                .taskId(historicTaskInstance.getId())
                .list();
        return History.builder()
                .taskId(historicTaskInstance.getId())
                .assignee(historicTaskInstance.getAssignee())
                .auditTime(historicTaskInstance.getEndTime())
                .auditResult(TaskUtil.getTaskAuditResult(localVariables))
                .data(TaskUtil.getProcessData(globalVariables))
                .comments(TaskUtil.getComments(comments))
                .businessKey(processList.get(0).getBusinessKey())
                .build();
    }
}
