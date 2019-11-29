package com.cmgun.api.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.common.Response;
import com.cmgun.api.model.History;
import com.cmgun.api.model.HistoryQueryRequest;
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
 * 历史记录管理
 *
 * @author chenqilin
 * @date 2019/7/29
 */
@FeignClient(value = "fuhsi-workflow-engine", path = "/history")
public interface ActHistoryService {

    @ApiOperation("历史任务查询")
    @PostMapping("/query")
    Response<PageResult<History>> query(@RequestBody @Validated @ApiParam(value = "查询参数", required = true) HistoryQueryRequest request);

    @ApiOperation("查询指定流程的历史任务")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", paramType = "query"),
        @ApiImplicitParam(name = "complete", value = "是否查询结束的任务，0-未结束，1-已结束", paramType = "query")
    })
    @GetMapping("/process")
    Response<List<History>> queryProcessHistory(@RequestParam("processInstanceId") String processInstanceId, @RequestParam("complete") Boolean complete);

    @ApiOperation("根据任务id查询详情")
    @GetMapping("/task")
    Response<History> queryTaskHistory(@RequestParam("taskId") String taskId);
}
