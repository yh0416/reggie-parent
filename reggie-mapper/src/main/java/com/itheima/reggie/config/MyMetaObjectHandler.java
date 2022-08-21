package com.itheima.reggie.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.reggie.common.EmployeeHolder;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Component
@Slf4j
//自定义元数据处理器
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    private HttpSession session;

    //insert时执行的
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("updateTime", new Date());
        //必须要强转类型,否则无法获得employee的属性
        //因为session只能应用于bs模式,所以要采用更通用的ThreadLocal,搭配拦截器使用
//        Employee employee = (Employee) session.getAttribute("STATUS_ENABLE");
        Employee employee = EmployeeHolder.get();
        if (employee != null) {
            metaObject.setValue("updateUser", EmployeeHolder.get().getId());
            metaObject.setValue("createUser", EmployeeHolder.get().getId());
        }
        User user = UserHolder.get();
        if (user != null) {
            metaObject.setValue("createUser", user.getId());
            metaObject.setValue("updateUser", user.getId());
        }

    }

    //update时执行的,更新操作
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", new Date());
//        Employee employee = (Employee) session.getAttribute("STATUS_ENABLE");
        Employee employee = EmployeeHolder.get();
        if (employee != null) {
            metaObject.setValue("updateUser", EmployeeHolder.get().getId());
        }
        User user = UserHolder.get();
        if (user != null) {
            metaObject.setValue("updateUser", user.getId());
        }
    }
}
