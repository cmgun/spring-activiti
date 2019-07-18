package com.cmgun.api.service;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProgressStartRequest;
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

/**
 * 流程管理
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@FeignClient(value = "workflow-server", path = "progress")
public interface ActProgressService {

    @ApiOperation("流程发布")
    @PostMapping(value = "deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response deploy(@ApiParam(value = "流程名称") @RequestParam(value = "progressName") String progressName
            , @ApiParam(value = "流程key") @RequestParam(value = "key") String key
            , @ApiParam(value = "流程文件")  @RequestPart(value = "file") MultipartFile file) throws IOException;

    @ApiOperation("流程开启")
    @PostMapping("start")
    Response start(@RequestBody @Validated ProgressStartRequest request);

    @ApiOperation("流程挂起")
    @PostMapping("suspend")
    Response suspend();

    @ApiOperation("流程重新激活")
    @PostMapping("activate")
    Response activate();
}
