package com.itheima.reggie.handler;

import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局统一异常处理器
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //自定义异常
    @ExceptionHandler(CustomException.class)
    public ResultInfo exceptionHandler(CustomException e) {
        e.printStackTrace();
        return ResultInfo.error(e.getMessage());
    }

    //处理可预见--唯一键重复的异常
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultInfo handlerDuplicatekeyException(Exception e){
        e.printStackTrace();
        return ResultInfo.error("您输入的已存在");
    }

    //处理不可预见
    @ExceptionHandler(Exception.class)
    public  ResultInfo handlerglobalException(Exception e){
        e.printStackTrace();
        return ResultInfo.error("网络开小差,请稍后重试");
    }
}
