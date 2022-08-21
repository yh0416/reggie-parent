package com.itheima.reggie.common;

import com.itheima.reggie.domain.User;

public class UserHolder {
    private static ThreadLocal<User> threadLocal=new ThreadLocal<>();
    //自己写方法,里面调用threadlocal的方法

    public static void set(User user){
        threadLocal.set(user);
    }

    public static User get(){
       return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
