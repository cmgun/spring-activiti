package com.cmgun.utils;

import com.cmgun.api.common.Request;
import org.aspectj.lang.JoinPoint;

/**
 * AOP工具类
 *
 * @author chenqilin
 * @Date 2019/7/22
 */
public class AopUtil {

    /**
     * 从切点中获取Request信息
     *
     * @param joinPoint 切点
     * @return Request
     */
    public static Request getRequestFromJoinPoint(JoinPoint joinPoint) {
        // 获取参数中的request
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Request) {
                return (Request) arg;
            }
        }
        return null;
    }
}
