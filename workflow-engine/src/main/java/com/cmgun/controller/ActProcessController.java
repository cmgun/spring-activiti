package com.cmgun.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.api.service.ActProcessService;
import com.cmgun.config.aspect.DuplicateResource;
import com.cmgun.entity.vo.ProcessVO;
import com.cmgun.service.BaseProcessService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 流程管理Service
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Api
@Slf4j
@RestController
@RequestMapping("process")
public class ActProcessController implements ActProcessService {

    @Autowired
    private BaseProcessService baseProcessService;

    @Override
    public Response deploy(String processName, String key, String source,MultipartFile file) throws IOException {
        // 必要校验
        Assert.notNull(processName,"process不能为空");
        Assert.notNull(key,"key不能为空");
        Assert.notNull(source,"source不能为空");
        // 文件是否合法
        String fileName = file.getOriginalFilename();
        Assert.notNull(fileName, "流程文件名不能为空");
        Assert.isTrue(fileName.endsWith("bpmn") || fileName.endsWith("bpmn20.xml")
                , "非bpmn流程文件");
        ProcessVO processVO = baseProcessService.deployProcess(processName, source.concat(key), file);
        return Response.success("部署成功", processVO);
    }

    @DuplicateResource
    @Override
    public Response start(ProcessStartRequest request) {
        ProcessVO processVO = baseProcessService.startProcess(request.getProcessDefinitionKey()
                , request.getBusinessKey(), request.getPayload());
        return Response.success("流程启动成功", processVO);
    }

    @Override
    public Response suspend() {
        return null;
    }

    @Override
    public Response activate() {
        return null;
    }
}
