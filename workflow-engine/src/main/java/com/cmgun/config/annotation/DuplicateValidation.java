package com.cmgun.config.annotation;

import java.lang.annotation.*;

/**
 * 标记该资源为不可重复请求的资源
 * 后续可扩展重复判断的类型，当前只支持一个sessionId不可重复进行请求
 *
 * @author chenqilin
 * @Date 2019/7/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DuplicateValidation {
}
