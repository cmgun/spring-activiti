package com.cmgun.consumer.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.service.ActProgressService;
import com.cmgun.consumer.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenqilin
 * @Date 2019/7/16
 */
@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private ActProgressService actProgressService;
    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("deploy")
    public Response deploy(String fileName, String progressName) throws Exception {
        Resource resource = applicationContext.getResource("classpath:/processes/" + fileName);
        if (!resource.exists()) {
            return Response.builder().code(Response.ERROR).build();
        }
        // 文件存在，生成multipartFile
        MultipartFile multipartFile = FileUtils.createMultipartFile("file", resource);
        return actProgressService.deploy(progressName, progressName + "-key", multipartFile);
    }


}
