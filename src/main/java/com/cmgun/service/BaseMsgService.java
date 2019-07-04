package com.cmgun.service;

import com.cmgun.entity.BaseMsg;
import com.cmgun.repository.BaseMsgRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenqilin
 * @Date 2019/7/2
 */
@Slf4j
@Service
public class BaseMsgService {

    @Autowired
    private BaseProcessService baseProcessService;
    @Autowired
    private BaseMsgRepository baseMsgRepository;

    public void addMsg(BaseMsg baseMsg) {
        baseMsgRepository.saveAndFlush(baseMsg);
        // 查找bean
        BaseMsg result = baseMsgRepository.findByMsg(baseMsg.getMsg());
        // msg内容作为唯一业务ID
        ProcessInstance processInstance = baseProcessService.startProcess(result, result.getMsg());
        log.info("processInstance:" + processInstance.getProcessDefinitionId());
    }

    public void addReject() {

    }

    public void addPass(String taskId, String groupName, String comments) {

    }

    public void addApprove() {

    }
}
