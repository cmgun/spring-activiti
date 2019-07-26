package com.cmgun.controller.handler;

import com.cmgun.api.common.Response;
import com.cmgun.config.aspect.exception.DuplicateRequestException;
import com.cmgun.exception.ActBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 *
 * @author chenqilin
 * @Date 2019/7/17
 */
@Slf4j
@ControllerAdvice
public class ErrorHandlerController {

    /**
     * 不合法参数异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Response IllegalArgumentExceptionHandler(HttpServletRequest request, IllegalArgumentException exception) {
        log.warn("IllegalArgumentExceptionHandler，url:{}", request.getRequestURI(), exception);
        return Response.builder()
                .code(Response.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

    }

    /**
     * 重复请求异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler({DuplicateRequestException.class, DuplicateKeyException.class})
    @ResponseBody
    public Response DuplicateRequestExceptionHandler(HttpServletRequest request, Exception exception) {
        log.warn("DuplicateRequestExceptionHandler，url:{}", request.getRequestURI(), exception);
        return Response.builder()
                .code(Response.REPEAT_REQUEST)
                .message(exception.getMessage())
                .build();
    }

    /**
     * 不合法参数异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response MethodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        StringBuilder errMsg = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errMsg.append(fieldError.getDefaultMessage()).append(";");
        }
        log.warn("MethodArgumentNotValidExceptionHandler，url:{}，errMsg:{}", request.getRequestURI()
                , errMsg.toString(), exception);
        return Response.builder()
                .code(Response.BAD_REQUEST)
                .message(errMsg.toString())
                .build();

    }

    /**
     * 业务异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(ActBusinessException.class)
    @ResponseBody
    public Response ActBusinessExceptionHandler(HttpServletRequest request, ActBusinessException exception) {
        log.warn("ActBusinessExceptionHandler，url:{}", request.getRequestURI(), exception);
        return Response.builder()
                .code(Response.BUSINESS_ERROR)
                .message(exception.getMessage())
                .build();
    }

    /**
     * 其他异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response ExceptionHandler(HttpServletRequest request, Exception exception) {
        log.warn("ExceptionHandler，url:{}", request.getRequestURI(), exception);
        return Response.builder()
                .code(Response.ERROR)
                .message("系统内部异常")
                .build();
    }
}
