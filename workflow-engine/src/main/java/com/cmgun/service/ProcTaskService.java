package com.cmgun.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.ProcessActiveTaskRequest;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.model.ToDoTaskRequest;

import java.util.List;

/**
 * 任务管理
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
public interface ProcTaskService {

    /**
     * 分页查询待办任务
     *
     * @param toDoTaskRequest 参数
     * @return 待办任务列表
     */
    PageResult<Task> queryToDoList(ToDoTaskRequest toDoTaskRequest);

    /**
     * 任务审批
     * 1. 必要校验：任务是否存在、用户/组是否有权限
     * 2. 任务认领，下个任务节点的上下文填充，任务完成
     *
     * @param taskAuditRequest 参数
     */
    void audit(TaskAuditRequest taskAuditRequest);

    /**
     * 查询流程当前激活的任务列表
     * @param request 查询参数
     * @return 任务列表
     */
    List<Task> queryActiveTaskByProcess(ProcessActiveTaskRequest request);
}
