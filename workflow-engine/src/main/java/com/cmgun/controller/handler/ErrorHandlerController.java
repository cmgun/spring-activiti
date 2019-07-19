package com.cmgun.controller.handler;

import com.cmgun.api.common.Response;
import com.cmgun.config.aspect.exception.DuplicateRequestException;
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
    @ExceptionHandler(DuplicateRequestException.class)
    @ResponseBody
    public Response DuplicateRequestExceptionHandler(HttpServletRequest request, DuplicateRequestException exception) {
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
        return Response.builder()
                .code(Response.BAD_REQUEST)
                .message(errMsg.toString())
                .build();

    }

    /**
     * 不合法参数异常
     *
     * @param request 请求
     * @param exception 异常
     * @return 响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response ExceptionHandler(HttpServletRequest request, Exception exception) {
        return Response.builder()
                .code(Response.ERROR)
                .message(exception.getMessage())
                .build();
    }
}
