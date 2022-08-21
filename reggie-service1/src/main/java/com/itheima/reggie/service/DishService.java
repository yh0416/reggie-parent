package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;

import java.util.List;

public interface DishService {
    //分页查询
    Page findByPage(Integer pageNum, Integer pageSize, String name);

    //新增菜品
    void save(Dish dish);

    //修改查询回显
    Dish findById(Long id);

    //修改
    void update(Dish dish);

    //删除
    void delete(List<Long> ids);

    //停售
    void stopSale(Integer status, List<Long> ids);

    //1-1新增套餐--添加套餐中的菜品
    List<Dish>  findDishList(Long categoryId,String name);
}
