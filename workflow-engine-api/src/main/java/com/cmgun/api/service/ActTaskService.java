package com.cmgun.api.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.common.Response;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.model.ToDoTaskRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 任务管理
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
@FeignClient(value = "workflow-engine", path = "task")
public interface ActTaskService {

    @ApiOperation("查询待办任务")
    @PostMapping("todoList")
    Response<PageResult<Task>> todoList(@RequestBody @Validated @ApiParam(value = "待办任务查询参数", required = true)
                                          ToDoTaskRequest request);

    @ApiOperation("任务审批")
    @PostMapping("audit")
    Response audit(@RequestBody @Validated @ApiParam(value = "任务审批参数", required = true)
                           TaskAuditRequest request);
}
