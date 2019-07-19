package com.cmgun.consumer.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProcessDeployRequest;
import com.cmgun.api.service.ActProcessService;
import com.cmgun.consumer.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author chenqilin
 * @Date 2019/7/16
 */
@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private ActProcessService actProcessNameService;
    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("deploy")
    public Response deploy(String fileName, String processName) throws Exception {
        Resource resource = applicationContext.getResource("classpath:/processes/" + fileName);
        if (!resource.exists()) {
            return Response.builder().code(Response.ERROR).build();
        }
        // 文件存在，生成multipartFile
        MultipartFile multipartFile = FileUtils.createMultipartFile("file", resource);
        ProcessDeployRequest request = new ProcessDeployRequest();
        // 请求流水号
        request.setRequestNo(UUID.randomUUID().toString());
        request.setSource("1");
        request.setProcessName(processName);
        request.setKey(processName + "-key");
        return actProcessNameService.deploy(processName, processName + "-key", multipartFile);
    }


}
