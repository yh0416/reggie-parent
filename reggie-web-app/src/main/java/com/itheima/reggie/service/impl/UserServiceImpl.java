package com.itheima.reggie.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.JwtUtil;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.SmsTemplate;
import com.itheima.reggie.domain.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private UserMapper userMapper;

    //发送短信
    @Override
    public void sendMsg(String phone) {
        //生成验证码
//        String code = RandomUtil.randomNumbers(6);
        String code = "6666";
        System.out.println(code);
        //将验证码手机号存入redis,设置时间期限
        redisTemplate.opsForValue().set("CODE_" + phone, code, 5, TimeUnit.MINUTES);
        //调用阿里云发送
        //需要时打开!!
        //  smsTemplate.sendSms(phone,code);
    }

    //登录注册
    @Override
    public ResultInfo login(String phone, String code) {
        //1.校验验证码,错误的返回error,正确的比对手机号
        //1-1获得redis中的验证码
        String redisCode = (String) redisTemplate.opsForValue().get("CODE_" + phone);
        if (!StringUtils.equals(redisCode, code)) {
            //验证码不正确
            return ResultInfo.error("您输入的验证码错误");
        }
        //2.比对手机号if正确&状态为1,登陆成功
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);
        //2-1查询到了手机号
        if (user != null) {
            if (user.getStatus() != 1) {
                return ResultInfo.error("当前账户状态不正常,请联系客服");
            }
        } else {
            //3.手机号查询不到,插入新的手机,设置状态为1
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userMapper.insert(user);
        }
        //4.将token保存到redis
        Map map = new HashMap();
        map.put("id", user.getId());
        String token = JwtUtil.createToken(map);
        redisTemplate.opsForValue().set("TOKEN_" + token, user, 1, TimeUnit.DAYS);
        //5.将token返回
        return ResultInfo.success(token);

    }

    //退出登录
    @Override
    public void logout(String token) {
        redisTemplate.delete("TOKEN_" + token);

    }

}
