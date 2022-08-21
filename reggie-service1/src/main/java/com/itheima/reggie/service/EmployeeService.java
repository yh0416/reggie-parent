package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;

import java.util.List;

public interface EmployeeService {
    //登录
    ResultInfo login(String username, String password);

    //员工列表
    List<Employee> find(String name);

    //新增员工
    void add(Employee employee);

    //查询员工(为了修改,根据id查询)
    Employee findById(Long id);

    //修改员工
    void update(Employee employee);
}
