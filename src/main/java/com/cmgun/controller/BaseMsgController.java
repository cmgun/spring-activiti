package com.cmgun.controller;

import com.cmgun.entity.BaseMsg;
import com.cmgun.entity.vo.HistoryVo;
import com.cmgun.entity.vo.TaskVo;
import com.cmgun.service.BaseMsgService;
import com.cmgun.service.BaseProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class BaseMsgController {

    @Autowired
    private BaseProcessService baseProcessService;

    @Autowired
    private BaseMsgService baseMsgService;

    @GetMapping("addMsg")
    public String startProcess(BaseMsg baseMsg) {
        baseMsgService.addMsg(baseMsg);
        return "ok";
    }

    @GetMapping("todoList")
    public List<TaskVo> queryToDoList(String groupName) {
        List<TaskVo> tasks = baseProcessService.queryGroupToDoTasks(groupName);
        log.info("groupName:" + groupName + ", todoList size:" + tasks.size());
        return tasks;
    }

    @GetMapping("addPass")
    public String addPass(String groupName, String taskId, String comment) {
        baseProcessService.approveTask(taskId, groupName, "true", comment);
        return "ok";
    }

    @GetMapping("addReject")
    public String addReject(String groupName, String taskId, String comment) {
        baseProcessService.approveTask(taskId, groupName, "false", comment);
        return "ok";
    }

    @GetMapping("history")
    public List<HistoryVo> queryAuditHistory(String groupName, String userId, String businessKey) {
        return baseProcessService.queryHistory(groupName, userId, businessKey);
    }

}
