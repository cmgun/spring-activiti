package com.cmgun.service;

import com.cmgun.entity.BaseMsg;
import com.cmgun.entity.TaskVo;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

public interface BaseProcessService {

    /**
     *
     * @param data
     * @param businessKey 防重的业务key
     * @return
     */
    ProcessInstance startProcess(Object data, String businessKey);

    List<TaskVo> queryGroupToDoTasks(String groupName);

    void approveTask(String taskId, String groupName, String approveResult, String comments);
}
