package com.cmgun.config.aspect;

import com.cmgun.api.common.Request;
import com.cmgun.config.annotation.DuplicateValidation;
import com.cmgun.config.aspect.exception.DuplicateRequestException;
import com.cmgun.utils.AopUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 防重请求处理AOP，只校验请求流水号
 *
 * @author chenqilin
 * @Date 2019/7/18
 */
@Aspect
@Slf4j
@Component
@Order(10)
public class DuplicateRequestAspect {

    /**
     * 重复请求标识 Redis Key
     */
    private static final String REPEAT_REQUEST_KEY = "workflow:duplicate_req:";

    /**
     * 重复请求标识 Redis 过期时间，单位秒
     */
    private static final long REPEAT_REQUEST_EXPIRATION = 300;

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
     * @param duplicateValidation
     */
    @Before("pointcut() && @annotation(duplicateValidation)")
    public void before(JoinPoint joinPoint, DuplicateValidation duplicateValidation) {
        Request request = AopUtil.getRequestFromJoinPoint(joinPoint);
        if (request == null) {
            // 没有符合格式的参数
            return;
        }
        // 校验流水号是否重复
        boolean repeatRequest = repeatRequest(request);
        if (repeatRequest) {
            throw new DuplicateRequestException("请求重复");
        }
    }

    /**
     * 判断是否重复请求
     * @param request 请求参数
     * @return true/false
     */
    private boolean repeatRequest(Request request) {
        if (StringUtils.isBlank(request.getRequestNo())) {
            return false;
        }
        // 公共key + 流水号查询
        String key = REPEAT_REQUEST_KEY + request.getRequestNo();
        Object result = stringRedisTemplate.opsForValue().get(key);
        if (result != null) {
            // 请求重复
            return true;
        }
        // 非重复请求，添加到redis，设置过期时间
        stringRedisTemplate.opsForValue().set(key, "1", REPEAT_REQUEST_EXPIRATION, TimeUnit.SECONDS);
        return false;
    }
}
