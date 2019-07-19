package com.cmgun.exception;

/**
 * 业务异常
 *
 * @author chenqilin
 * @Date 2019/7/19
 */
public class ActBusinessException extends RuntimeException {

    private static final long serialVersionUID = 4088001991951162L;

    public ActBusinessException(String msg) {
        super(msg);
    }

    public ActBusinessException(String msg, Throwable cause){
        super(msg, cause);
    }
}
