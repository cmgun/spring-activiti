package com.cmgun.consumer.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProgressDeployRequest;
import com.cmgun.api.service.ActProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenqilin
 * @Date 2019/7/16
 */
@RestController("consumer")
public class ConsumerController {

    @Autowired
    private ActProgressService actProgressService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("deploy")
    public Response deploy(String fileName, String progressName) throws Exception {
        Resource resource = applicationContext.getResource("classpath:/processes/" + fileName);
        if (!resource.exists()) {
            return Response.builder().code(Response.ERROR).build();
        }
        // 文件存在
        ProgressDeployRequest request = new ProgressDeployRequest();
        request.setProgressName(progressName);
        request.setKey(progressName + "key");
        request.setProgressFileStream(resource.getInputStream());
        return actProgressService.deploy(request);
    }
}
