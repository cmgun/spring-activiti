package com.cmgun.consumer.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.api.model.ToDoTaskRequest;
import com.cmgun.api.service.ActProcessService;
import com.cmgun.api.service.ActTaskService;
import com.cmgun.consumer.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private ApplicationContext applicationContext;
    @Autowired
    private ActProcessService actProcessNameService;
    @Autowired
    private ActTaskService actTaskService;


    @PostMapping("deploy")
    public Response deploy(String fileName, String processName) throws Exception {
        Resource resource = applicationContext.getResource("classpath:/processes/" + fileName);
        if (!resource.exists()) {
            return Response.builder().code(Response.ERROR).build();
        }
        // 文件存在，生成multipartFile
        MultipartFile multipartFile = FileUtils.createMultipartFile("file", resource);
        Response response = actProcessNameService.deploy(processName, processName + "-key", "1", "TestCategory", multipartFile);
        return response;
    }

    @PostMapping("start")
    public Response start(@RequestBody ProcessStartRequest request) {
        Response response = actProcessNameService.start(request);
        return response;
    }

    @PostMapping("todoList")
    public Response todoList(@RequestBody ToDoTaskRequest request) {
        Response response = actTaskService.todoList(request);
        return response;
    }
}
