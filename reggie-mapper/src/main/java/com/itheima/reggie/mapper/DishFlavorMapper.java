package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
//    //根据dish_id查找flavor
//    @Select("select *from DishFlavorx where dish_id=#{dishId}")
//    List<DishFlavor> findByDishId(List<Dish>dishes);
}