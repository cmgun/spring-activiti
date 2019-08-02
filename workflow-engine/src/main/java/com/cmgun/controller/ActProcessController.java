package com.cmgun.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.api.service.ActProcessService;
import com.cmgun.config.annotation.DuplicateValidation;
import com.cmgun.config.annotation.RequestPersistence;
import com.cmgun.entity.dto.DeployDTO;
import com.cmgun.entity.enums.BusiTypeEnum;
import com.cmgun.api.model.Process;
import com.cmgun.service.ProcessService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 流程管理
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Api(tags = "process")
@Slf4j
@RestController
@RequestMapping("process")
public class ActProcessController implements ActProcessService {

    @Autowired
    private ProcessService processService;

    @Override
    public Response<Process> deploy(String processName, String key, String source
            , String category, MultipartFile file) throws IOException {
        // 必要校验
        Assert.notNull(processName,"processName不能为空");
        Assert.notNull(key,"key不能为空");
        Assert.notNull(source,"source不能为空");
        Assert.notNull(category, "category不能为空");
        // 文件是否合法
        String fileName = file.getOriginalFilename();
        Assert.notNull(fileName, "流程文件名不能为空");
        Assert.isTrue(fileName.endsWith("bpmn") || fileName.endsWith("bpmn20.xml")
                , "非bpmn流程文件");
        DeployDTO deployDTO = DeployDTO.builder()
                .processName(processName)
                .key(source.concat("-").concat(key))
                .category(category)
                .file(file)
                .build();
        Process processDef = processService.deployProcess(deployDTO);
        return Response.success("部署成功", processDef);
    }

    @RequestPersistence(busiType = BusiTypeEnum.PROC_START)
    @DuplicateValidation
    @Override
    public Response<Process> start(ProcessStartRequest request) {
        Process processVO = processService.startProcess(request);
        return Response.success("流程启动成功", processVO);
    }
}
