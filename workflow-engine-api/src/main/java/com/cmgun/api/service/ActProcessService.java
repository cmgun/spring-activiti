package com.cmgun.api.service;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.Process;
import com.cmgun.api.model.ProcessActiveTaskRequest;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.api.model.Task;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 流程管理
 *
 * @author chenqilin
 * @date 2019/7/16
 */
@FeignClient(value = "fuhsi-workflow-engine", path = "/process")
public interface ActProcessService {

    @ApiOperation("流程发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processName", value = "部署名称", paramType = "query"),
            @ApiImplicitParam(name = "key", value = "部署key", paramType = "query"),
            @ApiImplicitParam(name = "source", value = "业务来源", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "部署类别", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "流程文件", paramType = "query", dataTypeClass = MultipartFile.class)
    })
    @PostMapping(value = "/deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<Process> deploy(@RequestParam(value = "processName") String processName
            , @RequestParam(value = "key") String key
            , @RequestParam(value = "source") String source
            , @RequestParam(value = "category") String category
            , @RequestPart("file") MultipartFile file) throws IOException;

    @ApiOperation("流程开启")
    @PostMapping("/start")
    Response<Process> start(@RequestBody @Validated @ApiParam(value = "流程开启参数", required = true) ProcessStartRequest request);

    @ApiOperation("某个流程当前激活的任务")
    @PostMapping("/query-active-tasks")
    Response<List<Task>> processActiveTasks(@RequestBody @Validated @ApiParam(value = "查询条件", required = true) ProcessActiveTaskRequest request);

    @ApiOperation("流程终止")
    @PostMapping("/end")
    Response<Void> endProcess(@RequestParam("processInstanceId") String processInstanceId, @RequestParam("reason") String reason);

}
