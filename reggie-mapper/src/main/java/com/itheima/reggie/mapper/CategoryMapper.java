package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    //分类列表
    List<Category> findAll();

    //新增
    void save(Category category);
    //修改分类
    void update(Category category);
    //删除分类
    //mapper查看id下是否有菜
    //1-1先在setmeal里面查
    Integer countSetMealId(Long id);
    //1-2在dish表里查
    Integer countSetDishId(Long id);
    //都没有的话,就删除
    void deleteById(Long id);
}
