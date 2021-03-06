package com.cmgun.utils;

import com.cmgun.exception.ActBusinessException;
import com.cmgun.exception.ActBusinessRepeatException;
import com.cmgun.exception.ActUnAuthorizationException;

/**
 * 异常处理工具类
 *
 * @author chenqilin
 * @date 2019/7/19
 */
public class ExceptionUtil {

    /**
     * 业务异常
     *
     * @param condition 条件，满足时抛指定异常
     * @param errMsg 异常信息
     */
    public static void businessException(boolean condition, String errMsg) {
        if (condition) {
            throw new ActBusinessException(errMsg);
        }
    }

    /**
     * 认证异常
     *
     * @param condition 条件，满足时抛指定异常
     * @param errMsg 异常信息
     */
    public static void authorizationException(boolean condition, String errMsg) {
        if (condition) {
            throw new ActUnAuthorizationException(errMsg);
        }
    }

    /**
     * 业务重复异常
     *
     * @param condition 条件，满足时抛指定异常
     * @param errMsg    异常信息
     */
    public static void businessRepeatException(boolean condition, String errMsg) {
        if (condition) {
            throw new ActBusinessRepeatException(errMsg);
        }
    }
}
