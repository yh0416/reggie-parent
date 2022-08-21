package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    @Autowired
    private HttpSession session;
    @Autowired
    private EmployeeService employeeService;

    //    登录功能
    @PostMapping("/employee/login")
    public ResultInfo login(@RequestBody Map<String, String> map) {
        //   1.接收参数
        String username = map.get("username");
        String password = map.get("password");
//    2.调用
        ResultInfo resultInfo = employeeService.login(username, password);
//    3.保存session
        if (resultInfo.getCode() == 1) {
            Employee employee = (Employee) resultInfo.getData();
            session.setAttribute("SESSION_EMPLOYEE", employee);
        }
//    4.返回resultinfo
        return resultInfo;
    }

    //    员工退出
    @PostMapping("/employee/logout")
    public ResultInfo logout() {
        //消除session
        session.invalidate();
        //返回result
        return ResultInfo.success(null);
    }

    //    员工列表
    @GetMapping("/employee/find")
    public ResultInfo find(String name) {
        //1.调用service
        List<Employee> employeeList = employeeService.find(name);
        //2.结果返回
        return ResultInfo.success(employeeList);
    }

    //新增员工
    @PostMapping("/employee")
    public ResultInfo add(@RequestBody Employee employee) {
        //调用
        employeeService.add(employee);
        //返回
        return ResultInfo.success(null);
    }

    //查询员工(为了修改,根据id查询)
    /*
    因为返回的
     */
    @GetMapping("/employee/{id}")
    public ResultInfo findById(@PathVariable("id") Long id){
        //调用service
        Employee employee=employeeService.findById(id);
        //返回
        return ResultInfo.success(employee);
    }

    //修改员工
    @PutMapping("/employee")
    public ResultInfo update(@RequestBody Employee employee){
        employeeService.update(employee);
        return ResultInfo.success(null);
    }
}
