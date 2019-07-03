package com.cmgun.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author chenqilin
 * @Date 2019/7/3
 */
@Slf4j
public class MsgRejectDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // 获取变量
        log.info("MsgPassDelegate, reject, " + execution.getVariables());
    }
}
