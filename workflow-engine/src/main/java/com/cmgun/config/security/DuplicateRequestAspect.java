package com.cmgun.config.security;

import com.cmgun.api.common.Request;
import com.cmgun.config.security.exception.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class DuplicateRequestAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 所有controller层
     */
    @Pointcut("execution(public * com.cmgun.controller..*(..))")
    public void pointcut() {
    }

    /**
     * 接口层处理前的请求防重校验
     *
     * @param joinPoint
     * @param duplicateResource
     */
    @Before("pointcut() && @annotation(duplicateResource)")
    public void before(JoinPoint joinPoint, DuplicateResource duplicateResource) {
        Request request = null;
        // 获取参数中的request
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Request) {
                request = (Request) arg;
            }
        }
        if (request == null) {
            // 没有符合格式的参数
            return;
        }
        // 校验流水号是否重复 TODO
        boolean repeatRequest = false;
        if (repeatRequest) {
            throw new DuplicateRequestException("请求重复");
        }
    }
}
