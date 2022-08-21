package com.itheima.reggie.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    //分类列表
    @Override
    public List<Category> findAll() {
        List<Category> categoryList = categoryMapper.findAll();
        return categoryList;
    }

    //新增分类
    @Override
    public void save(Category category) {
        //补全参数
        category.setId(IdUtil.getSnowflake(1, 1).nextId());
//        category.setCreateTime(new Date());
//        category.setUpdateTime(new Date());
//        category.setCreateUser(1L);
//        category.setUpdateUser(1L);
        //调用mapeer
        categoryMapper.save(category);
    }

    //修改分类

    @Override
    public void update(Category category) {
//        //1. 设置修改人和时间
//        category.setUpdateTime(new Date());
//        category.setUpdateUser(1L);

        //2. 调用mapper保存
        categoryMapper.update(category);
    }

    //删除分类
    @Override
    public void deleteById(Long id) {
        //mapper查看id下是否有菜
        //1-1先在setmeal里面查
        Integer count1 = categoryMapper.countSetMealId(id);
        if (count1 > 0) {
            throw new CustomException("当前分类下有套餐,还不可以删除哦!");
//            return ResultInfo.error("当前分类下有套餐,还不可以删除哦!");
        }
        //1-2在dish表里查
        Integer count2 = categoryMapper.countSetDishId(id);
        if (count2 > 0) {
            throw new CustomException("当前分类下有菜品,还不可以删除哦!");
//            return ResultInfo.error("当前分类下有菜品,还不可以删除哦!");
        }
        //都没有的话,就删除
        categoryMapper.deleteById(id);

    }

    //新增菜品
    @Override
    public List<Category> findByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType,type);
        List<Category> categoryList = categoryMapper.selectList(wrapper);
        return categoryList;
    }


}
