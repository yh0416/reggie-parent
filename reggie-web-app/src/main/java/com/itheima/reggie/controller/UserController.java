package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    //发送短信
    @Autowired
    private UserService userService;

    @PostMapping("/user/sendMsg")
    //@RequestBody接受请求体中的json数据转为对象,只能修饰对象和map,用于接收请求体中的参数
    public ResultInfo sendMsg(@RequestBody Map<String, String> map) {
        //接收参数
        String phone = map.get("phone");
        //调用
        userService.sendMsg(phone);
        //返回
        return ResultInfo.success("短信发送成功");
    }

    //登录注册
    @PostMapping("/user/login")
    public ResultInfo login(@RequestBody Map<String, String> map) {
        //接收参数
        String phone = map.get("phone");
        String code = map.get("code");
        //调用
        ResultInfo resultInfo = userService.login(phone, code);
        //返回
        return resultInfo;
    }

    //退出登录
    @PostMapping("/user/logout")
    public ResultInfo logout(@RequestHeader("Authorization") String token) {
        //处理token
        token = token.replace("Bearer", "").trim();
        userService.logout(token);
        return ResultInfo.success(null);
    }

}
