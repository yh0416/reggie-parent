package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Setmeal;

import java.util.List;

//套餐
public interface SetmealService {
    //查询列表
    Page findList(Integer pageNum, Integer pageSize, String name);

    //1-2新增套餐---新增套餐
//    Setmeal save(Setmeal setmeal);
    void save(Setmeal setmeal);

    //停售起售
    void ChangeStatus(Integer status, List<Long> ids);

    //删除套餐
    void deleteById(List<Long> ids);

    //    1-1修改套餐---回显
    Setmeal updateById(Long id);

    //    1-2修改套餐
    Setmeal update(Setmeal setmeal);

    //套餐查询
    List<Setmeal> findSetMeal(Long categoryId, Integer status);

    //套餐菜品查询
    List<Dish> findSetMealDish(Long id);
}
