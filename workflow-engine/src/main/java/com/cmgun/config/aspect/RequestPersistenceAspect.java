package com.cmgun.config.aspect;

import com.cmgun.api.common.Request;
import com.cmgun.config.annotation.RequestPersistence;
import com.cmgun.entity.BusiRequest;
import com.cmgun.entity.enums.RequestStatusEnum;
import com.cmgun.service.BusiRequestService;
import com.cmgun.utils.AopUtil;
import com.cmgun.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 防重请求处理AOP，只校验请求流水号
 *
 * @author chenqilin
 * @Date 2019/7/18
 */
@Aspect
@Slf4j
@Component
@Order(20)
public class RequestPersistenceAspect {

    @Autowired
    private BusiRequestService busiRequestService;

    /**
     * 所有controller层
     */
    @Pointcut("execution(public * com.cmgun.controller..*(..))")
    public void pointcut() {
    }

    /**
     * 持久化请求流水
     *
     * @param joinPoint
     * @param requestPersistence
     */
    @Before("pointcut() && @annotation(requestPersistence)")
    public void before(JoinPoint joinPoint, RequestPersistence requestPersistence) {
        Request request = AopUtil.getRequestFromJoinPoint(joinPoint);
        if (request == null) {
            // 没有符合格式的参数
            return;
        }
        // 持久化请求，独立与后续的流程事务
        BusiRequest busiRequest = new BusiRequest(requestPersistence.busiType(), request, RequestStatusEnum.INIT);
        ExceptionUtil.businessException(!busiRequestService.save(busiRequest), "请求流水持久化失败");
    }

    /**
     * 异常更新请求流水状态
     *
     * @param joinPoint
     * @param requestPersistence
     */
    @AfterThrowing(value = "pointcut() && @annotation(requestPersistence)", throwing = "ex")
    public void error(JoinPoint joinPoint, RequestPersistence requestPersistence, Throwable ex) {
        Request request = AopUtil.getRequestFromJoinPoint(joinPoint);
        if (request == null) {
            // 没有符合格式的参数
            return;
        }
        // 流水记录是否已存在，不存在则跳过
        int exists = busiRequestService.lambdaQuery()
                .eq(BusiRequest::getClientReqNo, request.getRequestNo())
                .eq(BusiRequest::getSource, request.getSource())
                .count();
        if (exists == 0) {
            return;
        }
        // 根据请求流水号更新记录
        boolean updateResult = busiRequestService.lambdaUpdate()
                .eq(BusiRequest::getClientReqNo, request.getRequestNo())
                .eq(BusiRequest::getSource, request.getSource())
                .update(BusiRequest.builder()
                        .status(RequestStatusEnum.FAIL.getValue())
                        .build());
        ExceptionUtil.businessException(!updateResult, "请求流水更新失败，clientReqNo:" + request.getRequestNo());
    }
}
