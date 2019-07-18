package com.cmgun.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProgressStartRequest;
import com.cmgun.api.service.ActProgressService;
import com.cmgun.config.security.DuplicateResource;
import com.cmgun.entity.vo.ProcessVO;
import com.cmgun.service.BaseProcessService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("progress")
public class ActProgressController implements ActProgressService {

    @Autowired
    private BaseProcessService baseProcessService;

//    @DuplicateResource
    @Override
    public Response deploy(String progressName, String key, MultipartFile file) throws IOException {
        // 必要校验
        Assert.notNull(progressName,"progressName不能为空");
        Assert.notNull(key,"key不能为空");
        // 文件是否合法
        String fileName = file.getOriginalFilename();
        Assert.notNull(fileName, "流程文件名不能为空");
        Assert.isTrue(fileName.endsWith("bpmn") || fileName.endsWith("bpmn20.xml")
                , "非bpmn流程文件");
        Deployment deployment = baseProcessService.deployProcess(progressName, key, file);
        if (deployment == null) {
            return Response.businessError("部署失败");
        }
        return Response.success("部署成功", new ProcessVO(deployment));
    }

    @DuplicateResource
    @Override
    public Response start(ProgressStartRequest request) {
        log.info("progress start");
        return null;
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
