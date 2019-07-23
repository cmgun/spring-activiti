package com.cmgun.service;

import com.cmgun.api.model.Task;
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
     * 查询待办任务
     *
     * @param toDoTaskRequest 请求
     * @return 待办任务列表
     */
    List<Task> queryToDoList(ToDoTaskRequest toDoTaskRequest);
}
