package com.cmgun.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author chenqilin
 * @Date 2019/7/3
 */
@Data
public class TaskVO {

    private String taskId;

    private Object variables;

    public TaskVO(String taskId, Map<String, Object> variables) {
        // 解析Task内容并封装成前端展示
        this.taskId = taskId;
        this.variables = variables.get("data");
    }
}
