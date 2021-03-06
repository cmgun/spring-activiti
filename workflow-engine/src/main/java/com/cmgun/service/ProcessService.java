package com.cmgun.service;

import com.cmgun.api.model.Process;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.entity.dto.DeployDTO;

import java.io.IOException;

/**
 * 流程处理Service
 */
public interface ProcessService {

    /**
     * 部署流程
     *
     * @param deployDTO 流程部署信息
     * @return 成功部署对应的流程定义
     */
    Process deployProcess(DeployDTO deployDTO) throws IOException;

    /**
     * 开启流程
     *
     * @param request 请求参数
     * @return 流程实例
     */
    Process startProcess(ProcessStartRequest request);

    /**
     * 终止流程
     * @param processInstanceId
     * @param reason
     */
    void endProcess(String processInstanceId, String reason);
}
