package com.cmgun.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author chenqilin
 * @Date 2019/7/2
 */
@Slf4j
@Service
public class MyService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Transactional
    public void startProcess(String userName) {
        runtimeService.startProcessInstanceByKey("oneTaskProcess", userName);
    }

    @Transactional
    public List<Task> getTasks(String assignee) {
        log.info("tasks count:" + taskService.createTaskQuery().count());
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

}