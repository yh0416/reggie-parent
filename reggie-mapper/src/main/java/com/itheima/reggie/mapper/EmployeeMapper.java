package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.itheima.reggie.domain.Employee;

import java.util.List;

@Repository//假注释
public interface EmployeeMapper extends BaseMapper<Employee> {
    //根据用户名查询
    Employee findByUsername(String username);

    //员工列表
    List<Employee> findByName(String name);

    //新增员工
    void add(Employee employee);

    //查询员工(为了修改,根据id查询)
    Employee findById(Long id);

    //修改员工
    void update(Employee employee);
}
