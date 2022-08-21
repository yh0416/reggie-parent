package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.domain.SetmealDish;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

//套餐
@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;

    //查询列表
    @Override
    public Page findList(Integer pageNum, Integer pageSize, String name) {
        //返回所有套餐,如果有姓名,返回符合条件的
        //1-1分页条件
        Page<Setmeal> page = new Page<>(pageNum, pageSize);
        //1-2业务条件
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        //1-3执行条件
        page = setmealMapper.selectPage(page, wrapper);//总是忘记,这俩是一个page!!!!!

        //根据setmeal里面的category_id,主键查询categroy,setCategoryName
        List<Setmeal> setmealList = page.getRecords();
        if (CollectionUtil.isNotEmpty(setmealList)) {
            for (Setmeal setmeal : setmealList) {
                Category category = categoryMapper.selectById(setmeal.getCategoryId());
                setmeal.setCategoryName(category.getName());
            }
        }
        return page;
    }

    //1-2新增套餐---新增套餐
    @Override
    public void save(Setmeal setmeal) {
        //1.插入新的setmeal
        setmealMapper.insert(setmeal);
        //2.补充插入setmeal_dish中的sort和setmeal_id
        List<SetmealDish> setmealDishes = setmeal.getSetmealDishes();
        //遍历之前判空
        if (CollectionUtil.isNotEmpty(setmealDishes)) {
            int i = 0;
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
                setmealDish.setSort(i++);
                setmealDishMapper.insert(setmealDish);
            }
        }
//        return setmeal;
    }

    //停售起售
    @Override
    public void ChangeStatus(Integer status, List<Long> ids) {
        //根据ids主键查询
        List<Setmeal> setmeals = setmealMapper.selectBatchIds(ids);
        //设置状态
        if (CollectionUtil.isNotEmpty(setmeals)) {
            for (Setmeal setmeal : setmeals) {
                setmeal.setStatus(status);
                setmealMapper.updateById(setmeal);
            }
        }
    }

    //    删除套餐(自己的版本)
    @Override
    public void deleteById(List<Long> ids) {
        //判断套餐是否在售
        List<Setmeal> setmeals = setmealMapper.selectBatchIds(ids);
        //取出状态
        if (CollectionUtil.isNotEmpty(setmeals)) {
            for (Setmeal setmeal : setmeals) {
                Integer status = setmeal.getStatus();
                if (status == 1) {
                    throw new CustomException("当前套餐在售,不可以删除哦!");
                }
                //删除setmeal_dish中相关的菜品,非主键删除
                LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
                setmealDishMapper.delete(wrapper);
            }
            setmealMapper.deleteBatchIds(ids);

        } else {
            throw new CustomException("选中的套餐为空");

        }
    }

    //    1-1修改套餐---回显
    @Override
    public Setmeal updateById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        //2. 根据套餐的category_id去category表中获取分类名称
        Category category = categoryMapper.selectById(setmeal.getCategoryId());
        setmeal.setCategoryName(category.getName());

        //3. 根据套餐id去setmeal_dish中查询套餐信息
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(wrapper1);
        setmeal.setSetmealDishes(setmealDishList);
        return setmeal;
    }

    //    1-2修改套餐
    @Override
    public Setmeal update(Setmeal setmeal) {
        //1.插入新的setmeal
        setmealMapper.updateById(setmeal);
        //2.根据 setmeal_id删除setmeal_dish中得相关信息(条件删除)
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        setmealDishMapper.delete(wrapper);

        //3.重新插入setmeal_dish中的sort和setmeal_id
        List<SetmealDish> setmealDishes = setmeal.getSetmealDishes();
        //遍历之前判空
        if (CollectionUtil.isNotEmpty(setmealDishes)) {
            int i = 0;
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
                setmealDish.setSort(i++);
                setmealDishMapper.insert(setmealDish);
            }
        }
        return setmeal;
    }

    //套餐查询
    @Override
    public List<Setmeal> findSetMeal(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, categoryId)
                .eq(Setmeal::getStatus, status);
        List<Setmeal> setmeals = setmealMapper.selectList(wrapper);
        for (Setmeal setmeal : setmeals) {
            System.out.println(setmeal);
        }
        return setmeals;
    }

    //套餐菜品查询
    @Override
    public List<Dish> findSetMealDish(Long id) {
        //根据setmeal的id去setmeal_dish表中获得dish_id
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(wrapper);
        //根据dish_id去dish表中获得相同id的dish
        ArrayList<Dish> dishList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(setmealDishList)) {
            for (SetmealDish setmealDish : setmealDishList) {
                Dish dish = dishMapper.selectById(setmealDish.getDishId());
                dish.setCopies(setmealDish.getCopies());
                dishList.add(dish);
            }
        }
        return dishList;
    }
}


//=======================================================================老师的删除版本========================================================================================
//    @Override
//    public void deleteByIds(List<Long> ids) {
//        //1. 判断ids中是否有status=1
//        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Setmeal::getStatus, 1)
//                .in(Setmeal::getId, ids);
//        Integer num = setmealMapper.selectCount(wrapper);
//        if (num > 0) {
//            throw new CustomException("有套餐正处于售卖状态,不允许删除");
//        }
////
//        //2. 删除setmeal_dish表中setmeal_id在ids中的(条件删除
//        //构建删除条件 delete from setmeal_dish where setmeal_id in #{ids}
//        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
//        wrapper1.in(SetmealDish::getSetmealId, ids);
//        setmealDishMapper.delete(wrapper1);
////
//        //3. 再删除id在ids中  DELETE FROM setmeal WHERE id IN ( ? , ? )
//        setmealMapper.deleteBatchIds(ids);
//    }



