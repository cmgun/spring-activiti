package com.cmgun.entity;

import lombok.Data;
import org.activiti.engine.task.Task;

import java.util.Map;

/**
 * @author chenqilin
 * @Date 2019/7/3
 */
@Data
public class TaskVo {

    private String taskId;

    private Object variables;

    public TaskVo(String taskId, Map<String, Object> variables) {
        // 解析Task内容并封装成前端展示
        this.taskId = taskId;
        this.variables = variables.get("data");
    }
}
