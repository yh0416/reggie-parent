package com.itheima.reggie.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.druid.util.StringUtils;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itheima.reggie.domain.Employee;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    //登录
    @Override
    public ResultInfo login(String username, String password) {
        //1.根据username校验
        Employee employee = employeeMapper.findByUsername(username);
        if (employee == null) {
            return ResultInfo.error("用户名输入错误");
        }
//        2.根据status校验
//        todo ==在比较引用类型时,不是比较的地址值吗
        if (employee.getStatus() == Employee.STATUS_DISABLE) {
            return ResultInfo.error("该用户被禁止登录");
        }
//        3.根据passowrd校验
        String passwordWithMd5 = SecureUtil.md5(password);
        if (!StringUtils.equals(passwordWithMd5, employee.getPassword())) {
            return ResultInfo.error("密码错误");
        }
        return ResultInfo.success(employee);

    }

    //员工列表
    @Override
    public List<Employee> find(String name) {
        List<Employee> employeeList = employeeMapper.findByName(name);
        return employeeList;
    }

    //新增员工
    @Override
    public void add(Employee employee) {
        //1.补齐参数
        //雪花算法
        employee.setId(IdUtil.getSnowflake(1,1).nextId());
        //其余参数
        //密码要加密
        employee.setPassword(SecureUtil.md5("123"));
        employee.setStatus(Employee.STATUS_ENABLE);
//        employee.setCreateTime(new Date());
//        employee.setUpdateTime(new Date());
//        employee.setUpdateUser(1L);
//        employee.setCreateUser(1L);

        //2.调用mapper
        employeeMapper.add(employee);

    }


    //查询员工(为了修改,根据id查询)
    @Override
    public Employee findById(Long id) {
         //调用mapper
        Employee employee=employeeMapper.findById(id);
        return employee;
    }

    //修改员工
    @Override
    public void update(Employee employee) {
        //设置两个字段
//        employee.setUpdateTime(new Date());
//        employee.setUpdateUser(1L);
        //调用
        employeeMapper.update(employee);
    }

}
