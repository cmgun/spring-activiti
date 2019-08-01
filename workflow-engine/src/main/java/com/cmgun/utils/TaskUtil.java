package com.cmgun.utils;

import com.cmgun.api.common.TaskContext;
import com.cmgun.api.model.Comment;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.entity.dto.AuditDTO;
import com.google.common.collect.Lists;
import org.activiti.engine.history.HistoricVariableInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程任务工具类
 *
 * @author chenqilin
 * @Date 2019/7/26
 */
public class TaskUtil {

    /**
     * 任务参与组
     */
    public final static String CANDIDATE_GROUPS = "candidateGroups";

    /**
     * 任务参与用户
     */
    public final static String CANDIDATE_USERS = "candidateUsers";
    /**
     * 流程业务数据，贯穿整个流程生命周期
     */
    public final static String PROCESS_DATA = "data";
    /**
     * 任务审批结果
     */
    public final static String RESULT = "result";
    /**
     * 当前任务节点的本地变量
     */
    public final static String LOCAL_DATA = "localData";
    /**
     * 审批信息
     */
    public final static String AUDIT_INFO = "auditInfo";

    /**
     * 流程上下文填充
     *
     * @param context 下一个任务的上下文
     * @param taskContext 请求参数
     */
    public static void globalTaskContext(Map<String, Object> context, TaskContext taskContext) {
        // 下个任务的参与组
        context.put(CANDIDATE_GROUPS, taskContext.getCandidateGroups());
        // 流程业务数据
        if (taskContext.getGlobalPayload() != null) {
            context.put(PROCESS_DATA, taskContext.getGlobalPayload());
        }
    }

    /**
     * 获取审批任务的本地上下文
     *
     * @param request 审批任务参数
     * @return 本地上下文
     */
    public static Map<String, Object> auditTaskLocalContext(TaskAuditRequest request) {
        AuditDTO auditDTO = AuditDTO.builder()
                .auditor(request.getAuditor())
                .auditorName(request.getAuditorName())
                .auditResult(request.getAuditResult())
                .comment(request.getComment())
                .build();
        Map<String, Object> localContext = new HashMap<>();
        localContext.put(AUDIT_INFO, auditDTO);
        localContext.put(RESULT, request.getAuditResult());
        localContext.put(LOCAL_DATA, request.getTaskContext().getLocalPayload());
        return localContext;
    }

    /**
     * 从历史变量中获取审批处理结果
     *
     * @param localVariables 历史变量
     * @return 审批处理结果
     */
    public static Object getTaskAuditResult(List<HistoricVariableInstance> localVariables) {
        for (HistoricVariableInstance variable : localVariables) {
            if (AUDIT_INFO.equals(variable.getVariableName())) {
                AuditDTO auditDTO = (AuditDTO) variable.getValue();
                return auditDTO.getAuditResult();
            }
        }
        return null;
    }

    /**
     * 从历史变量中获取流程变量结果
     *
     * @param globalVariables 历史变量
     * @return 审批处理结果
     */
    public static Object getProcessData(List<HistoricVariableInstance> globalVariables) {
        for (HistoricVariableInstance variable : globalVariables) {
            if (PROCESS_DATA.equals(variable.getVariableName())) {
                return variable.getValue();
            }
        }
        return null;
    }

    /**
     * 从流程变量中获取业务数据
     *
     * @param variables 流程变量
     * @return 业务数据
     */
    public static Object getProcessData(Map<String, Object> variables) {
        if (variables != null) {
            return variables.get(PROCESS_DATA);
        }
        return null;
    }

    /**
     * 获取任务审批备注记录
     *
     * @param rawComments 原始审批备注
     * @return 审批备注记录
     */
    public static List<Comment> getComments(List<org.activiti.engine.task.Comment> rawComments) {
        List<Comment> comments = Lists.newArrayList();
        for (org.activiti.engine.task.Comment rawComment : rawComments) {
            Comment comment = Comment.builder()
                    .userId(rawComment.getUserId())
                    .comment(rawComment.getFullMessage())
                    .build();
            comments.add(comment);
        }
        return comments;
    }

    /**
     * 封装上个任务的审批信息
     *
     * @param builder builder
     * @param localVariables 本地变量
     */
    public static void parseLastTaskAuditInfo(Task.TaskBuilder builder, Map<String, Object> localVariables) {
        if (localVariables == null || !localVariables.containsKey(AUDIT_INFO)) {
            return;
        }
        AuditDTO auditDTO = (AuditDTO) localVariables.get(AUDIT_INFO);
        builder.auditResult(auditDTO.getAuditResult())
                .auditor(auditDTO.getAuditor())
                .auditorName(auditDTO.getAuditorName())
                .comment(auditDTO.getComment());
    }
}
