package com.itheima.reggie.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.reggie.common.JwtUtil;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从请求中取出token,处理后,如果token为空,不予通过
        String token = request.getHeader("Authorization");
        //1-2处理token
        token = token.replace("Bearer", "").trim();
        log.info("token="+token);
        if (StringUtils.isEmpty(token)) {
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            String json = new ObjectMapper().writeValueAsString(resultInfo);
            response.getWriter().write(json);

            return false;
        }
        //2.解析token,出现异常,不予通过
        try {
            JwtUtil.parseToken(token);
        } catch (Exception e) {
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            String json = new ObjectMapper().writeValueAsString(resultInfo);
            response.getWriter().write(json);

            return false;
        }
        //3.redis中找不到对应token,不予通过
        User user = (User) redisTemplate.opsForValue().get("TOKEN_" + token);
        if (user == null) {
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            String json = new ObjectMapper().writeValueAsString(resultInfo);
            response.getWriter().write(json);

            return false;
        }
        //4.最后,前面三个条件都满足,续期,重新放入redis,续期
        redisTemplate.opsForValue().set("TOKEN_" + token, user, 1, TimeUnit.DAYS);
        UserHolder.set(user);
        return true;
    }

    //离开服务器之前,记得删除threadlocal中的东西,要不然下次登录,之前的东西还在
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}
