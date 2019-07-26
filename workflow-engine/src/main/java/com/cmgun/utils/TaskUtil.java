package com.cmgun.utils;

import com.cmgun.api.common.TaskContext;
import com.cmgun.api.model.TaskAuditRequest;

import java.util.HashMap;
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
    private final static String CANDIDATE_GROUPS = "candidateGroups";
    /**
     * 流程业务数据，贯穿整个流程生命周期
     */
    private final static String PROCESS_DATA = "data";
    /**
     * 任务上下文，审批用户名称
     */
    private final static String AUDITOR_NAME = "auditorName";
    /**
     * 任务审批用户id
     */
    private final static String AUDITOR = "auditor";
    /**
     * 任务审批结果
     */
    private final static String RESULT = "result";


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
        return localContext;
    }
}
