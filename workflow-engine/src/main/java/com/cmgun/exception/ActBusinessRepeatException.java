package com.cmgun.exception;

/**
 * 业务重复执行异常
 *
 * @author chenqilin
 * @date 2019/7/19
 */
public class ActBusinessRepeatException extends RuntimeException {

    public ActBusinessRepeatException(String msg) {
        super(msg);
    }

    public ActBusinessRepeatException(String msg, Throwable cause){
        super(msg, cause);
    }
}
