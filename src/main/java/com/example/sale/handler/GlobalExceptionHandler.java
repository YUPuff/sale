package com.example.sale.handler;


import com.example.sale.common.BusinessException;
import com.example.sale.common.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 用于捕捉校验字段产生的异常并返回给前端具体错误信息
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result validExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return Result.failure(bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * 捕捉自定义业务异常
     * @param exception
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException exception){
        return Result.failure(exception.getMessage());
    }

    /**
     * 捕捉授权异常
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public Result authorizationExceptionHandler(Exception exception){
        return Result.failure("您暂时无法查看此页内容");
    }

    /**
     * 捕捉全局异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception exception){
        return Result.failure("无数据");
    }
}
