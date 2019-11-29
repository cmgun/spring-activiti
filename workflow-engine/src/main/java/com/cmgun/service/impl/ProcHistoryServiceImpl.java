package com.cmgun.service.impl;

import com.google.common.collect.Lists;
import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.History;
import com.cmgun.api.model.HistoryQueryRequest;
import com.cmgun.entity.dto.AuditDTO;
import com.cmgun.mapper.ActHistoryMapper;
import com.cmgun.service.ProcHistoryService;
import com.cmgun.utils.ExceptionUtil;
import com.cmgun.utils.PageUtil;
import com.cmgun.utils.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程历史任务
 *
 * @author chenqilin
 * @date 2019/7/29
 */
@Slf4j
@Service
public class ProcHistoryServiceImpl implements ProcHistoryService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActHistoryMapper actHistoryMapper;

    @Override
    public PageResult<History> queryHistory(HistoryQueryRequest request) {
        // 任务候选组，非空时忽略其他查询条件
        if (CollectionUtils.isNotEmpty(request.getCandidateGroup())) {
            // 根据任务候选组查询，重写查询语句
            return queryHistoryByCandidateGroup(request);
        }
        // 其余情况
        HistoricTaskInstanceQuery query = getHistoryQuery(request);
        // 查询结果
        List<HistoricTaskInstance> historicTasks = Lists.newArrayList();
        // 总数
        long count = PageUtil.query(query, request, historicTasks);
        // 结果填充
        return getHistoryPageResult(request, count, historicTasks);
    }

    @Override
    public PageResult<History> queryHistoryByCandidateGroup(HistoryQueryRequest request) {
        boolean needPageQuery = BooleanUtils.isNotTrue(request.getQueryAll());
        long count;
        List<HistoricTaskInstance> historicTasks = Lists.newArrayList();
        if (needPageQuery) {
            count = actHistoryMapper.countByCandidateGroup(request);
            if (PageUtil.isOutOfRange(count, request)) {
                return PageResult.empty(request, count);
            }
            // 分页查询
            historicTasks.addAll(actHistoryMapper.queryByCandidateGroupByPage(request, request.getStartRow(), request.getPageSize()));
            // 结果填充
        } else {
            // 不分页查询
            historicTasks.addAll(actHistoryMapper.queryByCandidateGroup(request));
            count = historicTasks.size();
        }

        return getHistoryPageResult(request, count, historicTasks);
    }

    /**
     * 封装历史任务查询条件
     *
     * @param request 参数
     * @return 查询条件
     */
    private HistoricTaskInstanceQuery getHistoryQuery(HistoryQueryRequest request) {
        // 任务用户信息不能全为空
        ExceptionUtil.businessException(request.getAssignee() == null && request.getCandidateUser() == null
                , "用户查询条件不能都为空");
        // 按任务结束时间倒序查询
        HistoricTaskInstanceQuery historyQuery = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        // 流程类型
        if (StringUtils.isNotBlank(request.getProcCategory())) {
            historyQuery.processCategoryIn(Lists.newArrayList(request.getProcCategory()));
        }
        // 任务id
        if (StringUtils.isNotBlank(request.getTaskId())) {
            historyQuery.taskId(request.getTaskId());
        }
        // 任务定义id
        if (StringUtils.isNotBlank(request.getTaskDefId())) {
            historyQuery.taskDefinitionKey(request.getTaskDefId());
        }
        // 任务类型
        if (StringUtils.isNotBlank(request.getCategory())) {
            historyQuery.taskCategory(request.getCategory());
        }
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
        // 任务创建时间
        if (request.getTaskCreateTimeBegin() != null) {
            historyQuery.taskCreatedAfter(request.getTaskCreateTimeBegin());
        }
        if (request.getTaskCreateTimeEnd() != null) {
            historyQuery.taskCreatedBefore(request.getTaskCreateTimeEnd());
        }
        return historyQuery;
    }

    /**
     * 填充查询分页结果
     *
     * @param request 分页请求
     * @param count 总数
     * @param historicTasks 分页原始数据
     * @return 分页结果
     */
    private PageResult<History> getHistoryPageResult(HistoryQueryRequest request, long count
            , List<HistoricTaskInstance> historicTasks) {
        List<History> result = Lists.newArrayList();
        for (HistoricTaskInstance historicTaskInstance : historicTasks) {
            History history = getHistory(historicTaskInstance);
            if (history != null) {
                result.add(history);
            }
        }
        return PageResult.result(request, count, result);
    }

    /**
     * 获取历史任务信息
     *
     * @param historicTaskInstance 历史任务原生实例
     * @return 历史任务信息
     */
    private History getHistory(HistoricTaskInstance historicTaskInstance) {
        if (historicTaskInstance.getProcessInstanceId() == null) {
            log.warn("流程实例不存在，任务id:{}", historicTaskInstance.getId());
            return null;
        }
        // 对应流程
        List<HistoricProcessInstance> processList = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(historicTaskInstance.getProcessInstanceId())
                .list();
        if (CollectionUtils.isEmpty(processList)) {
            log.warn("流程实例不存在，任务id:{}", historicTaskInstance.getId());
            return null;
        }
        // 流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(processList.get(0).getProcessDefinitionId())
            .list();
        if (CollectionUtils.isEmpty(processDefinitions)) {
            log.warn("流程定义不存在，流程定义key:{}", processList.get(0).getProcessDefinitionKey());
            return null;
        }

        // 审批备注
        List<Comment> comments = taskService.getTaskComments(historicTaskInstance.getId());
        // 查询流程参数
        List<HistoricVariableInstance> globalVariables = historyService.createHistoricVariableInstanceQuery()
                .excludeTaskVariables()
                .processInstanceId(historicTaskInstance.getProcessInstanceId())
                .list();
        // 查询当前任务审批参数
        List<HistoricVariableInstance> localVariables = historyService.createHistoricVariableInstanceQuery()
                .taskId(historicTaskInstance.getId())
                .list();
        // 当前任务审批信息
        AuditDTO auditDTO = TaskUtil.getTaskAuditInfo(localVariables);
        List<com.cmgun.api.model.Comment> commentList = TaskUtil.getComments(comments);
        // 被删除流程强制停止的任务
        if (historicTaskInstance.getEndTime() != null && auditDTO == null) {
            // 补充停止流程的信息
            auditDTO = AuditDTO.builder()
                .auditTime(historicTaskInstance.getEndTime())
                // 固定为拒绝
                .auditResult("2")
                .build();
            com.cmgun.api.model.Comment deleteComment = com.cmgun.api.model.Comment.builder()
                .userId("0")
                .comment(historicTaskInstance.getDeleteReason())
                .build();
            commentList.add(deleteComment);
        }
        return History.builder()
            .taskId(historicTaskInstance.getId())
            .taskName(historicTaskInstance.getName())
            .taskDefKey(historicTaskInstance.getTaskDefinitionKey())
            .procNameSpace(processDefinitions.get(0).getCategory())
            .assignee(historicTaskInstance.getAssignee())
            .auditor(auditDTO != null ? auditDTO.getAuditor() : null)
            .auditorName(auditDTO != null ? auditDTO.getAuditorName() : null)
            .auditResult(auditDTO != null ? auditDTO.getAuditResult() : null)
            .auditTime(historicTaskInstance.getEndTime())
            .processInstanceId(historicTaskInstance.getProcessInstanceId())
            .createTime(historicTaskInstance.getCreateTime())
            .data(TaskUtil.getProcessData(globalVariables))
            .comments(commentList)
            .businessKey(processList.get(0).getBusinessKey())
            .build();
    }

    @Override
    public List<History> queryProcessHistory(String processInstanceId, Boolean complete) {
        HistoricTaskInstanceQuery historyQuery = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByHistoricTaskInstanceEndTime()
            .desc();
        if (BooleanUtils.isTrue(complete)) {
            historyQuery.finished();
        }
        // 查询结果
        List<HistoricTaskInstance> historicTasks = historyQuery.list();
        List<History> result = Lists.newArrayList();
        for (HistoricTaskInstance historicTaskInstance : historicTasks) {
            History history = getHistory(historicTaskInstance);
            if (history != null) {
                result.add(history);
            }
        }
        return result;
    }

    @Override
    public History queryHistoryDetail(String taskId) {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
            .taskId(taskId)
            .list();
        if (CollectionUtils.isEmpty(historicTasks)) {
            return null;
        }
        // 封装结果
        HistoricTaskInstance taskInstance = historicTasks.get(0);
        History history = getHistory(taskInstance);
        if (history != null) {
            // 获取上一个历史任务的信息
            HistoricTaskInstance lastTask = actHistoryMapper.queryLastTask(taskInstance.getProcessInstanceId(), taskId);
            if (lastTask != null) {
                // 查询当前任务审批参数
                List<HistoricVariableInstance> localVariables = historyService.createHistoricVariableInstanceQuery().taskId(lastTask.getId()).list();
                // 上一个任务审批信息
                AuditDTO auditDTO = TaskUtil.getTaskAuditInfo(localVariables);
                history.setLastAuditor(auditDTO != null ? auditDTO.getAuditor() : null);
                history.setLastAuditorName(auditDTO != null ? auditDTO.getAuditorName() : null);
                history.setLastComment(auditDTO != null ? auditDTO.getComment() : null);
                history.setLastAuditTime(auditDTO != null ? auditDTO.getAuditTime() : null);
                history.setLastAuditResult(auditDTO != null ? auditDTO.getAuditResult() : null);
            }
        }
        return history;
    }
}
