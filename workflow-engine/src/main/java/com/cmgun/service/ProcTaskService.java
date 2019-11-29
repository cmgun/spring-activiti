package com.cmgun.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.*;

import java.util.List;

/**
 * 任务管理
 *
 * @author chenqilin
 * @date 2019/7/23
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
     * 根据任务id查询任务详情
     *
     * @param taskId 任务id
     * @return 任务详情
     */
    Task queryDetail(String taskId);

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
     *
     * @param request 查询参数
     * @return 任务列表
     */
    List<Task> queryActiveTaskByProcess(ProcessActiveTaskRequest request);

    /**
     * 查询任务的权限分配列表
     *
     * @param taskIds 任务id
     * @return 任务身份权限列表
     */
    List<TaskIdentity> queryIdentityLink(List<String> taskIds);
}
