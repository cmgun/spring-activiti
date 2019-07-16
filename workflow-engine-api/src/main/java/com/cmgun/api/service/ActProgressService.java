package com.cmgun.api.service;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProgressDeployRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 流程管理
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@FeignClient(value = "workflow-server", url = "actProgress")
//@Service
public interface ActProgressService {

    @ApiOperation("流程发布")
    @PostMapping("deploy")
    Response deploy(ProgressDeployRequest request);

    @ApiOperation("流程开启")
    @PostMapping("start")
    Response start();

    @ApiOperation("流程挂起")
    @PostMapping("suspend")
    Response suspend();

    @ApiOperation("流程重新激活")
    @PostMapping("activate")
    Response activate();
}
