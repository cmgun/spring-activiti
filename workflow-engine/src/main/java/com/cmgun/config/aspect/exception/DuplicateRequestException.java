package com.cmgun.config.aspect.exception;

/**
 * 重复请求异常
 *
 * @author chenqilin
 * @Date 2019/7/18
 */
public class DuplicateRequestException extends RuntimeException {

    private static final long serialVersionUID = -7456130539009426616L;

    public DuplicateRequestException(String msg) {
        super(msg);
    }

    public DuplicateRequestException(String msg, Throwable cause){
        super(msg, cause);
    }
}
