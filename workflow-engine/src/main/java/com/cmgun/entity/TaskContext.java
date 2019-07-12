package com.cmgun.entity;

import lombok.Data;

/**
 * @author chenqilin
 * @Date 2019/7/11
 */
@Data
public class TaskContext<T> {

    private String taskId;
    private String groupName;
    private T approveResult;
    private String comment;

    public TaskContext(String taskId, String groupName, T approveResult, String comment) {
        this.taskId = taskId;
        this.groupName = groupName;
        this.approveResult = approveResult;
        this.comment = comment;
    }
}
