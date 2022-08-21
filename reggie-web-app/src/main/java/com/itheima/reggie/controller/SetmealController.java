package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    //套餐查询
    @GetMapping("/setmeal/list")
    public ResultInfo findSetMeal(Long categoryId, Integer status) {
        List<Setmeal> setmeals = setmealService.findSetMeal(categoryId, status);
        return ResultInfo.success(setmeals);
    }

    //套餐菜品查询
    @GetMapping("/setmeal/dish/{id}")
    public  ResultInfo findSetMealDish(@PathVariable("id") Long id){//setmeal的id
        List<Dish> dishList=setmealService.findSetMealDish(id);
        return ResultInfo.success(dishList);
    }
}
