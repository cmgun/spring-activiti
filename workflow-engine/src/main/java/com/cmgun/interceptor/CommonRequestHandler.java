package com.cmgun.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用请求拦截器，简单的请求防重处理。
 * 通过sessionId + url + 参数 hashcode，进行简单短期防重判定
 *
 * @author chenqilin
 * @Date 2019/7/18
 */
@Configuration
public class CommonRequestHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return false;
    }
}
