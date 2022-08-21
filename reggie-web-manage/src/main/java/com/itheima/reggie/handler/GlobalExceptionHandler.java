package com.itheima.reggie.handler;

import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody//记得加
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateKeyException.class)
    //处理唯一键重复的异常
    public ResultInfo handlerDuplicatekeyException(Exception e) {
        //打印日志
        e.printStackTrace();
        if (e.getMessage().contains("idx_category_name")){
            return ResultInfo.error("当前输入的分类名重复,请重新输入");
        }
        return ResultInfo.error("当前输入的名称重复,请重新输入");
    }

    //自定义异常
    @ExceptionHandler(CustomException.class)
    public ResultInfo handlerCustomException(Exception e){
        //日志
        e.printStackTrace();
        //获取自定义的提示错误语句
        return ResultInfo.error(e.getMessage());
    }
//    处理非预见的异常
    @ExceptionHandler(Exception.class)
    public ResultInfo handlerglobalException(Exception e) {
        e.printStackTrace();
        return ResultInfo.error("网络开小差,请稍候");
    }
}
