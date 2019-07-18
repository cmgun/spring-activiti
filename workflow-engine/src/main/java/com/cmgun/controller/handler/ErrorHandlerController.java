package com.cmgun.controller.handler;

import com.cmgun.api.common.Response;
import com.cmgun.config.security.exception.DuplicateRequestException;
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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Response IllegalArgumentExceptionHandler(HttpServletRequest request, IllegalArgumentException exception) {
        return Response.builder()
                .code(Response.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

    }

    @ExceptionHandler(DuplicateRequestException.class)
    @ResponseBody
    public Response DuplicateRequestExceptionHandler(HttpServletRequest request, DuplicateRequestException exception) {
        return Response.builder()
                .code(Response.REPEAT_REQUEST)
                .message(exception.getMessage())
                .build();

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response ExceptionHandler(HttpServletRequest request, IllegalArgumentException exception) {
        return Response.builder()
                .code(Response.ERROR)
                .message(exception.getMessage())
                .build();

    }
}
