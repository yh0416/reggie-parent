package com.itheima.reggie.service.impl;//package com.itheima.reggie.service.impl;
//
//import cn.hutool.core.collection.CollectionUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.itheima.reggie.domain.Category;
//import com.itheima.reggie.domain.Dish;
//import com.itheima.reggie.domain.DishFlavor;
//import com.itheima.reggie.mapper.CategoryMapper;
//import com.itheima.reggie.mapper.DishFlavorMapper;
//import com.itheima.reggie.mapper.DishMapper;
//import com.itheima.reggie.service.DishService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DishServiceImpl implements DishService {
//    @Autowired
//    private DishMapper dishMapper;
//    @Autowired
//    private CategoryMapper categoryMapper;
//    @Autowired
//    private DishFlavorMapper dishFlavorMapper;
//
//    @Override
//    public Page findByPage(Integer pageNum, Integer pageSize, String name) {
////1.分页查询所有dish
//        //1-1设置分页条件
//        Page<Dish> page = new Page<>(pageNum, pageSize);
//        //1-2设置业务条件,select* from dish where name like
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        //StringUtils是 org.apache.commons.lang3的,专门判断字符串是否既非空又非空串
//        //传递了name的查询时,走这条语句
//        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
//        //1-3执行查询
//        page = dishMapper.selectPage(page, wrapper);
//
//// 2.遍历,查询所有的category品类名
//        //获取当前页数据
//        List<Dish> dishList = page.getRecords();
//        if (CollectionUtil.isNotEmpty(dishList)) {
//            for (Dish dish : dishList) {
//                Category category = categoryMapper.selectById(dish.getCategoryId());
//                //专门用来承接category的name,所以用@TableFileId(exist=false),这个数据只存在于实体类的属性中
//                dish.setCategoryName(category.getName());
//
////3.遍历查询所有的口味dish_flavor中的dish_id
////                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
////                wrapper1.eq(DishFlavor::getDishId,dish.getId());
////                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
////                dish.setFlavors(dishFlavorList);
//            }
//        }
//        System.out.println(page);
//        return page;
//    }
//}


//=====================================================================第二遍代码=======================================================================================

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //分页查询
    @Override
    public Page findByPage(Integer pageNum, Integer pageSize, String name) {
        //1.分页查询所有dish
        //1-1.设置分页条件 page
        Page<Dish> page = new Page<>(pageNum, pageSize);
        //1-2.设置业务条件,select* from dish where name like
        //todo 查询所有,没有传name,怎么走的逻辑呢
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //1-3.执行查询
        //page和第一步的page是一个,这是一个赋值的过程
        page = dishMapper.selectPage(page, wrapper);
        //page必须要getrecords才能获得当前页面数据
        List<Dish> dishList = page.getRecords();
        if (CollectionUtil.isNotEmpty(dishList)) {//CollectionUtil的这个方法专门看集合是否为空
            for (Dish dish : dishList) {
                //2.遍历循环设置category的名字
                Category category =
                        categoryMapper.selectById(dish.getCategoryId());
                dish.setCategoryName(category.getName());
                //3.循环遍历设置dish-flavor的名字
//                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
//                wrapper1.eq(DishFlavor::getId, dish.getId());
//                List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wrapper1);//条件查询,返回多条记录
//                dish.setFlavors(dishFlavors);
            }
        }
        System.out.println(page);
        return page;
    }

    //新增菜品
    @Override
    public void save(Dish dish) {
        //插入菜品
        dishMapper.insert(dish);
        //根据菜品id补全dish_flavor
        //遍历前先确定非空
        if (CollectionUtil.isNotEmpty(dish.getFlavors())) {
            //取出Flavors
            List<DishFlavor> flavors = dish.getFlavors();
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
                //塞回Flavors
                dishFlavorMapper.insert(flavor);
            }
        }
    }

    //修改查询回显
    @Override
    public Dish findById(Long id) {
        Dish dish = dishMapper.selectById(id);
        //回显category
        Category category = categoryMapper.selectById(dish.getCategoryId());
        dish.setCategoryName(category.getName());
        //回显dish_flavor,口味
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wrapper);
        dish.setFlavors(dishFlavors);
        return dish;


    }

    //修改
    @Override
    public void update(Dish dish) {
        //更新dish表中的内容
        dishMapper.updateById(dish);
        /*
        更新口味表,因为口味表可能删除,可能增加,更新是指原有的选项不变,在里面做修改,所以不能用update,
        鉴于这种复合的情况,只能全部根据dish_id删除,再insert;
         */
        //1-1更新口味表,删除
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        dishFlavorMapper.delete(wrapper);
        //1-2更新口味表,遍历插入新的
        List<DishFlavor> flavors = dish.getFlavors();
        if (CollectionUtil.isNotEmpty(flavors)) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
                dishFlavorMapper.insert(flavor);
            }
        }


    }

    //删除---自己的版本
//    @Override
//    public void delete(List<Long> ids) {
//        //1.查看是否有在售状态的
//        List<Dish> dishes = dishMapper.selectBatchIds(ids);
//        for (Dish dish : dishes) {
//            if (dish.getStatus() == 1) {
//                throw new CustomException("当前菜品在售,不能删除");
//            }
//            break;//break是跳出循环,继续向下走;continue是跳出此次循环,继续循环
//        }
//        //2.删除口味信息
//        //根据dish_id查找flavor
//        List<DishFlavor> dishFlavorList = dishFlavorMapper.findByDishId(dishes);
//        for (DishFlavor dishFlavor : dishFlavorList) {
//            dishFlavorMapper.deleteById(dishFlavor.getId());
//        }
//        //3.删除菜品
//        dishMapper.deleteBatchIds(ids);
//    }

    //删除---老师的版本
    @Override
    public void delete(List<Long> ids) {
        //select *from dish where status='1' and id in #{ids}
        //1.查看是否有在售
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1).in(Dish::getId, ids);
        Integer count = dishMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CustomException("当前菜品为在售状态,不能删除");
        }

        //2.删除口味信息
        List<Dish> dishes = dishMapper.selectBatchIds(ids);
        if (CollectionUtil.isNotEmpty(dishes)) {
            for (Dish dish : dishes) {
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId, dish.getId());
                dishFlavorMapper.delete(wrapper1);
            }
            //3.删除菜品
            dishMapper.deleteBatchIds(ids);
        }
    }

    //停售
    @Override
    public void stopSale(Integer status, List<Long> ids) {
        List<Dish> dishes = dishMapper.selectBatchIds(ids);
        if (CollectionUtil.isNotEmpty(dishes)) {
            for (Dish dish : dishes) {
                dish.setStatus(status);
                dishMapper.updateById(dish);
            }
        }

    }

    //1-1新增套餐--添加套餐中的菜品
    @Override
    public   List<Dish>  findDishList(Long categoryId,String name) {
        //根据categoryId,查找dish,返回
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus,1)
                .eq(categoryId != null, Dish::getCategoryId, categoryId)//分类id
                .like(StringUtils.isNotEmpty(name),Dish::getName,name);
        List<Dish> dishes = dishMapper.selectList(wrapper);
        //======================添加:根据dish_id查找对应口味========================================
        if (CollectionUtil.isNotEmpty(dishes)){
            for (Dish dish : dishes) {
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId,dish.getId());
                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
                dish.setFlavors(dishFlavorList);
            }
        }
        return dishes;
    }


}
//=================================================break & continue=======================================================================
//    @Test
//    public void test1() {
//        int sum = 0;
//        for (int i = 1; i <= 10; i++) {
//            System.out.println("begin i = " + i);
//            if (i % 2 == 0) {
//                continue; // continue语句会结束本次循环
//            }
//            sum = sum + i;
//            System.out.println("end i = " + i);
//        }
//        System.out.println(sum); // 25
//    }

//    @Test
//    public void test2() {
//        for (int i = 1; i <= 10; i++) {
//            System.out.println("i = " + i);
//            for (int j = 1; j <= 10; j++) {
//                System.out.println("j = " + j);
//                if (j >= i) {
//                    break;
//                }
//            }
//            // break跳到这里
//            System.out.println("breaked");
//        }

//    }



