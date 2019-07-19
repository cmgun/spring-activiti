package com.cmgun.service;

import com.cmgun.api.common.TaskContext;
import com.cmgun.entity.vo.HistoryVO;
import com.cmgun.entity.vo.ProcessVO;
import com.cmgun.entity.vo.TaskVO;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 流程处理Service
 */
public interface BaseProcessService {

    /**
     * 部署流程
     *
     * @param processName 流程文件名称，bpmn或bpmn20.xml结尾
     * @param key 流程key
     * @param multipartFile 流程文件
     * @return 成功部署对应的流程定义
     */
    ProcessVO deployProcess(String processName, String key, MultipartFile multipartFile) throws IOException;

    /**
     * 开启流程
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey 业务key
     * @param processData 流程数据，贯穿整个流程生命周期
     * @return 流程实例
     */
    ProcessVO startProcess(String processDefinitionKey, String businessKey, Object processData);

    List<TaskVO> queryGroupToDoTasks(String groupName);

    void approveTask(String taskId, String groupName, String approveResult, String comments);

    List<HistoryVO> queryHistory(String userId, String businessKey);
}
