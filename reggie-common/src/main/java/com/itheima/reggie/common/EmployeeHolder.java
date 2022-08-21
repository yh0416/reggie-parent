package com.itheima.reggie.common;

import com.itheima.reggie.domain.Employee;

//ThreadLocal操作类
public class EmployeeHolder {
    private static ThreadLocal<Employee> threadLocal = new ThreadLocal<>();

    //公有静态方法方便调用,自己写方法,里面调用threadlocal的方法
    public static void set(Employee employee) {
        threadLocal.set(employee);
    }
    public static Employee get(){
         return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }

}
