package com.cmgun.controller;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.common.Response;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.model.ToDoTaskRequest;
import com.cmgun.api.service.ActTaskService;
import com.cmgun.config.annotation.DuplicateValidation;
import com.cmgun.config.annotation.RequestPersistence;
import com.cmgun.entity.enums.BusiTypeEnum;
import com.cmgun.service.ProcTaskService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务管理
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
@Api
@Slf4j
@RestController
@RequestMapping("task")
public class ActTaskController implements ActTaskService {

    @Autowired
    private ProcTaskService taskService;

    @Override
    public Response<PageResult<Task>> todoList(ToDoTaskRequest request) {
        PageResult<Task> pageResult = taskService.queryToDoList(request);
        return Response.success("查询成功", pageResult);
    }

    @RequestPersistence(busiType = BusiTypeEnum.TASK_AUDIT)
    @DuplicateValidation
    @Override
    public Response audit(TaskAuditRequest request) {
        taskService.audit(request);
        return Response.success("任务审批成功");
    }
}
