package com.cmgun.api.common;

import lombok.Data;

/**
 * 流程任务上下文
 *
 * @author chenqilin
 * @Date 2019/7/19
 */
@Data
public class TaskContext {

    /**
     * 下个任务节点的参与组，多个用逗号分隔
     */
    private String candidateGroups;
}
