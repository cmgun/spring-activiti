package com.cmgun.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 请求日志记录AOP
 *
 * @author chenqilin
 * @Date 2019/7/19
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    /**
     * 所有controller层
     */
    @Pointcut("@within(io.swagger.annotations.Api)")
    public void pointcut() {
    }

    /**
     * 接口层处理前的请求防重校验
     *
     * @param joinPoint
     */
    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 类名
        String clazzName = joinPoint.getTarget().getClass().getName();
        // 方法名
        String methodName = joinPoint.getSignature().getName();
        // 参数
        String args = Arrays.toString(joinPoint.getArgs());
        log.info("开始处理请求, Class:{}, method:{}, args:{}", clazzName, methodName, args);
        long startTime = System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            log.warn("请求执行异常, Class:{}, method:{}, args:{}", clazzName, methodName, args, e);
            throw e;
        } finally {
            log.info("请求结束, Class:{}, method:{}, args:{}, time:{}"
                    , clazzName, methodName, args, System.currentTimeMillis() - startTime);
        }
    }
}
