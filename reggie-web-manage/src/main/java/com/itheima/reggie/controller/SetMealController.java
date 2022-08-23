package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class SetMealController {
    @Autowired
    private SetmealService setmealService;

    //查询列表
    @GetMapping("/setmeal/page")
    public ResultInfo findList(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize, String name)//name是套餐的name
    {
        Page page = setmealService.findList(pageNum, pageSize, name);
        return ResultInfo.success(page);
    }

    //    1-1修改套餐---回显
    @GetMapping("/setmeal/{id}")
    public ResultInfo updateById(@PathVariable("id") Long id) {
        Setmeal setmeal = setmealService.updateById(id);
        return ResultInfo.success(setmeal);
    }

    //==================================增删改的redis操作===============================================
    //1-2新增套餐---新增套餐
    @CacheEvict(value = "setmeal",allEntries = true)
    @PostMapping("/setmeal")
    public ResultInfo save(@RequestBody Setmeal setmeal) {
//        Setmeal setmeal1=setmealService.save(setmeal);
        setmealService.save(setmeal);
        return ResultInfo.success(null);
    }

    //停售起售
    @PostMapping("/setmeal/status/{status}")
    @CacheEvict(value = "setmeal",allEntries = true)
    public ResultInfo ChangeStatus(@PathVariable("status") Integer status,
                                   @RequestParam("ids") List<Long> ids) {
        setmealService.ChangeStatus(status, ids);
        return ResultInfo.success(null);
    }

    //删除套餐
    @DeleteMapping("/setmeal")
    @CacheEvict(value = "setmeal",allEntries = true)
    public ResultInfo deleteById(@RequestParam("ids") List<Long> ids) {
        setmealService.deleteById(ids);
        return ResultInfo.success(null);
    }



    //    1-2修改套餐
    @CacheEvict(value = "setmeal",allEntries = true)
    @PutMapping("/setmeal")
    public ResultInfo update(@RequestBody Setmeal setmeal) {
        Setmeal setmeal1=setmealService.update(setmeal);
        return ResultInfo.success(null);
    }
}


