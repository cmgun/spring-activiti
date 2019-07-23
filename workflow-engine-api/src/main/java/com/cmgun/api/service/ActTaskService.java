package com.cmgun.api.service;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.ToDoTaskRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 任务管理
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
@FeignClient(value = "workflow-server", path = "task")
public interface ActTaskService {

    @ApiOperation("查询待办任务")
    @PostMapping("todoList")
    Response<List<Task>> todoList(@RequestBody @Validated ToDoTaskRequest request);
}
