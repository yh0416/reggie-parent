package com.itheima.reggie.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    //套餐菜品显示
    @GetMapping("/dish/list")
    public ResultInfo findDishList(Long categoryId, Integer status) {
        //如果是一整块数据,在redis中通常用string来存储,如果修改某一条或者多条,用set
        //0.配置键
        String key = "dish_" + categoryId;
        //1.用redis查询
        List<Dish> dishList = (List<Dish>) redisTemplate.opsForValue().get(key);
        if (CollectionUtil.isNotEmpty(dishList)) {
            //1-1如果redis有,直接返回前端
            return ResultInfo.success(dishList);
        } else {
            //1-1如果redis中没有,就去mysql中查找,添加给redis,并且返回前端
            dishList = dishService.findDishList(categoryId, null);
            redisTemplate.opsForValue().set(key,dishList);
            return ResultInfo.success(dishList);
        }
    }
}
