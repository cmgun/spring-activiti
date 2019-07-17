package com.cmgun.entity.vo;

import lombok.Data;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Comment;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author chenqilin
 * @Date 2019/7/5
 */
@Data
public class HistoryVO {

    /**
     * task id
     */
    private String id;

    /**
     * 业务参数
     */
    private Object data;

    private String auditId;

    private String comment;

    private String auditResult;

    private Date auditTime;

    public HistoryVO(HistoricTaskInstance taskInstance, List<Comment> comments
            , List<HistoricVariableInstance> processVariables, List<HistoricVariableInstance> localVariables) {
        Assert.notNull(taskInstance, "HistoricTaskInstance is null");
        // 审批人
        this.auditId = taskInstance.getAssignee();
        // 任务id
        id = taskInstance.getId();
        // 审批时间
        auditTime = taskInstance.getEndTime();
        // 流程业务参数
        for (HistoricVariableInstance variable : processVariables) {
            if ("data".equals(variable.getVariableName())) {
                data = variable.getValue();
                break;
            }
        }
        // 任务参数，获取审批结果
        for (HistoricVariableInstance variable : localVariables) {
            if ("result".equals(variable.getVariableName())) {
                auditResult = (String) variable.getValue();
                break;
            }
        }
        // 审批意见
        if (CollectionUtils.isNotEmpty(comments)) {
            this.comment = comments.get(0).getFullMessage();
        }
    }
}
