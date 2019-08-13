package com.cmgun.consumer.service;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.service.ActTaskService;
import com.cmgun.consumer.entity.BaseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author chenqilin
 * @date 2019/7/25
 */
@Service
public class ConsumerService {

    @Autowired
    private BaseMsgService baseMsgService;
    @Autowired
    private ActTaskService actTaskService;


    public void saveMsg(String msg) {
        // 添加一条记录
        BaseMsg baseMsg = new BaseMsg();
        baseMsg.setMsg(msg);
        baseMsg.setState(0);
        // 本地事务
        baseMsgService.save(baseMsg);
    }

    @Transactional
    public Response audit(TaskAuditRequest request) {
        // 添加一条记录
        BaseMsg baseMsg = new BaseMsg();
        baseMsg.setMsg(String.valueOf(new Date()));
        baseMsg.setState(0);
        // 本地事务
        baseMsgService.save(baseMsg);
        // 正常情况下，发请求前要保留请求流水号，初始状态

        // 后续的请求发送不影响业务数据持久化
        // 外部事务，流程更新
        Response response = actTaskService.audit(request);
        // 响应码判断，根据不同情况更新业务数据状态
        // 响应结果为终态时，更新请求流水记录。非终态则需要进行重试补偿
        return response;
    }
}
