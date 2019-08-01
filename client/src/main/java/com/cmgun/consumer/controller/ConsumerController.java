package com.cmgun.consumer.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.enums.SourceEnum;
import com.cmgun.api.model.HistoryQueryRequest;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.model.ToDoTaskRequest;
import com.cmgun.api.service.ActHistoryService;
import com.cmgun.api.service.ActProcessService;
import com.cmgun.api.service.ActTaskService;
import com.cmgun.consumer.service.ConsumerService;
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
    private ActProcessService actProcessService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ActHistoryService actHistoryService;
    @Autowired
    private ConsumerService consumerService;

    @PostMapping("deploy")
    public Response deploy(String fileName, String processName) throws Exception {
        Resource resource = applicationContext.getResource("classpath:/processes/" + fileName);
        if (!resource.exists()) {
            return Response.builder().code(Response.ERROR).build();
        }
        // 文件存在，生成multipartFile
        MultipartFile multipartFile = FileUtils.createMultipartFile("file", resource);
        Response response = actProcessService.deploy(processName, processName + "-key", SourceEnum.INVOICE_LOAN.getCode()
                , "01-loanApply", multipartFile);
        return response;
    }

    @PostMapping("start")
    public Response start(@RequestBody ProcessStartRequest request) {
        // 保存业务数据，msg作为业务key
        consumerService.saveMsg(request.getBusinessKey());
        Response response = actProcessService.start(request);
        return response;
    }

    @PostMapping("todoList")
    public Response todoList(@RequestBody ToDoTaskRequest request) {
        Response response = actTaskService.todoList(request);
        return response;
    }

    @PostMapping("audit")
    public Response audit(@RequestBody TaskAuditRequest request) {
        return consumerService.audit(request);
    }

    @PostMapping("history")
    public Response history(@RequestBody HistoryQueryRequest request) {
        Response response = actHistoryService.query(request);
        return response;
    }
}
