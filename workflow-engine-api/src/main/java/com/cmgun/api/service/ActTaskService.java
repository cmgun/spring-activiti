package com.cmgun.api.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.common.Response;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.model.TaskIdentity;
import com.cmgun.api.model.ToDoTaskRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 任务管理
 *
 * @author chenqilin
 * @date 2019/7/23
 */
@FeignClient(value = "fuhsi-workflow-engine", path = "/task")
public interface ActTaskService {

    @ApiOperation("分页查询待办任务")
    @PostMapping("/todo")
    Response<PageResult<Task>> todoList(@RequestBody @Validated @ApiParam(value = "待办任务查询参数", required = true) ToDoTaskRequest request);

    @ApiOperation("任务审批")
    @PostMapping("/audit")
    Response audit(@RequestBody @Validated @ApiParam(value = "任务审批参数", required = true) TaskAuditRequest request);

    @ApiOperation("任务审批")
    @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "流程实例id", paramType = "query"), @ApiImplicitParam(name = "complete", value = "是否查询结束的任务", paramType = "query")})
    @GetMapping("/task-identity")
    Response<List<TaskIdentity>> queryIdentityLink(@RequestParam("taskIds") List<String> taskIds);

    @ApiOperation("根据任务id查询详情")
    @GetMapping("/detail")
    Response<Task> queryDetail(@RequestParam("taskId") String taskId);

}
