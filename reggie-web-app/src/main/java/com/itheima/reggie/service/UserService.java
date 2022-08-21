package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;

public interface UserService {
    //发送短信
    void sendMsg(String phone);

    //登录注册
    ResultInfo login(String phone, String code);

    //退出登录
    void logout(String token);
}
