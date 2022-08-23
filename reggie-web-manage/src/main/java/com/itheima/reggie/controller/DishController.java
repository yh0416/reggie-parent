package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    //分页查询
    @GetMapping("/dish/page")
    //区分@param,@param是mybatis中,①方法中包含多个参数,②需要起别名
    public ResultInfo findByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 String name) {
        Page page = dishService.findByPage(pageNum, pageSize, name);
        return ResultInfo.success(page);

    }

    //1-1新增套餐查询--添加套餐中的菜品
    @GetMapping("/dish/list")
    public ResultInfo findDishList(Long categoryId, String name) {
        List<Dish> dishList = dishService.findDishList(categoryId, name);
        return ResultInfo.success(dishList);
    }

    //修改查询回显
    @GetMapping("/dish/{id}")
    //@PathVariable标注在请求参数之前.从请求路径中取值,赋值给方法参数
    public ResultInfo findById(@PathVariable("id") Long id) {
        Dish dish = dishService.findById(id);
        //TODO 为什么有的时候返回null,有的时候又把数据都返回
        return ResultInfo.success(dish);

    }


//========================================以下是增删改的操作,要做redis缓存=====================================================================================================//


    //新增菜品
    @PostMapping("/dish")
    public ResultInfo save(@RequestBody Dish dish) {
        //清除缓存
        redisTemplate.delete(redisTemplate.keys("dish_*"));
        dishService.save(dish);
        return ResultInfo.success(null);

    }


    //修改
    @PutMapping("/dish")
    public ResultInfo update(@RequestBody Dish dish) {
        //清除缓存
        redisTemplate.delete(redisTemplate.keys("dish_*"));
        dishService.update(dish);
        return ResultInfo.success(null);
    }

    //删除
    @DeleteMapping("/dish")
    public ResultInfo delete(@RequestParam("ids") List<Long> ids) {
        //清除缓存
        redisTemplate.delete(redisTemplate.keys("dish_*"));
        dishService.delete(ids);
        return ResultInfo.success(null);
    }

    //停售
    @PostMapping("/dish/status/{status}")
    public ResultInfo stopSale(@PathVariable("status") Integer status,
                               @RequestParam("ids") List<Long> ids) {
        //清除缓存
        redisTemplate.delete(redisTemplate.keys("dish_*"));
        dishService.stopSale(status, ids);
        return ResultInfo.success(null);
    }


}
