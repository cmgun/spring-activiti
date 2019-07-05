package com.cmgun.service;

import com.cmgun.entity.vo.HistoryVo;
import com.cmgun.entity.vo.TaskVo;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

public interface BaseProcessService {

    ProcessInstance startProcess(Object data, String businessKey);

    List<TaskVo> queryGroupToDoTasks(String groupName);

    void approveTask(String taskId, String groupName, String approveResult, String comments);

    List<HistoryVo> queryHistory(String groupName, String userId, String businessKey);
}
