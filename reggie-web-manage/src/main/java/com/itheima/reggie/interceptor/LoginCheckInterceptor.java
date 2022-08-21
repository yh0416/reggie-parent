package com.itheima.reggie.interceptor;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.EmployeeHolder;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //有session,放行
        Employee employee = (Employee) request.getSession().getAttribute("SESSION_EMPLOYEE");
        if (employee != null) {
            EmployeeHolder.set(employee);
            return true;
        }
        //没有session,打印语句,不放行,前端规定见到NOTLOGIN就跳转页面,在js requset.js
        String json = JSON.toJSONString(ResultInfo.error("NOTLOGIN"));
        response.getWriter().write(json);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EmployeeHolder.remove();
    }
}
