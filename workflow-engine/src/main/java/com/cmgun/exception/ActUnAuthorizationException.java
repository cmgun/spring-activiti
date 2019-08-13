package com.cmgun.exception;

/**
 * 无权限操作异常
 *
 * @author chenqilin
 * @date 2019/7/25
 */
public class ActUnAuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 8631108163117899949L;

    public ActUnAuthorizationException(String msg) {
        super(msg);
    }

    public ActUnAuthorizationException(String msg, Throwable cause){
        super(msg, cause);
    }
}
