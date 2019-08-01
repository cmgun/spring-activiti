package com.cmgun.utils;

import com.cmgun.api.common.TaskContext;
import com.cmgun.api.model.Comment;
import com.cmgun.api.model.TaskAuditRequest;
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
     * 任务上下文，审批用户名称
     */
    public final static String AUDITOR_NAME = "auditorName";
    /**
     * 任务审批用户id
     */
    public final static String AUDITOR = "auditor";
    /**
     * 任务审批结果
     */
    public final static String RESULT = "result";
    /**
     * 当前任务节点的本地变量
     */
    public final static String LOCAL_DATA = "localData";


    /**
     * 初始化下一个任务节点的上下文
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
        Map<String, Object> localContext = new HashMap<>();
        localContext.put(RESULT, request.getAuditResult());
        localContext.put(AUDITOR_NAME, request.getAuditorName());
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
            if (RESULT.equals(variable.getVariableName())) {
                return variable.getValue();
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
}
