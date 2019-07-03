package com.cmgun.delegate;

import com.cmgun.entity.BaseMsg;
import com.cmgun.repository.BaseMsgRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author chenqilin
 * @Date 2019/7/3
 */
@Slf4j
public class MsgPassDelegate implements JavaDelegate {

    @Autowired
    private BaseMsgRepository baseMsgRepository;

    @Override
    public void execute(DelegateExecution execution) {
        // 获取变量
        log.info("MsgPassDelegate, pass, " + execution.getVariables());
        // 获取业务id
        BaseMsg baseMsg = (BaseMsg) execution.getVariable("data");
        // 更新状态
        baseMsgRepository.updateState(baseMsg.getId(), 0, 1);
    }
}
