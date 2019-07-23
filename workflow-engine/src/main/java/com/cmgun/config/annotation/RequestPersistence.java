package com.cmgun.config.annotation;

import com.cmgun.entity.enums.BusiTypeEnum;

import java.lang.annotation.*;

/**
 * 标记该方法的请求流水号需要持久化到DB
 *
 * @author chenqilin
 * @Date 2019/7/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequestPersistence {

    /**
     * @return 业务类型
     * @see com.cmgun.entity.enums.BusiTypeEnum
     */
    BusiTypeEnum busiType() default BusiTypeEnum.DEFAULT;
}
