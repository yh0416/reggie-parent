package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.mapper.CartMapper;
import com.itheima.reggie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    //新增购物车
    @Override
    public Cart addCart(Cart cart) {
        //1.首先根据user_id和dish_id、setmeal_id来查看是否购物车有
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, UserHolder.get().getId())
                .eq(cart.getDishId() != null, Cart::getDishId, cart.getDishId())
                .eq(cart.getSetmealId() != null, Cart::getSetmealId, cart.getSetmealId());
        Cart cartSearchOut = cartMapper.selectOne(wrapper);
        //2.有,增加number
        if (cartSearchOut != null) {
            //看清是给谁的number加1
            cartSearchOut.setNumber(cartSearchOut.getNumber() + 1);
            //记得操作数据库
            cartMapper.updateById(cartSearchOut);
            //记得if,else两种条件返回不同的结果
            return cartSearchOut;
            //3.没有,添加进去,补充user_id,number,create_time
        } else {
            cart.setUserId(UserHolder.get().getId());
            cart.setNumber(1);
            cart.setCreateTime(new Date());
            cartMapper.insert(cart);
            return cart;
        }
    }

    //查询购物车
    @Override
    public List<Cart> finCart() {
        //根据user_id查询
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());
        List<Cart> cartList = cartMapper.selectList(wrapper);
        return cartList;
    }

    //修改购物车
    @Override
    public void updateCart(Cart cart) {
        //根据不为空的值,查到要修改的菜品/套餐
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(cart.getDishId() != null, Cart::getDishId, cart.getDishId());
        wrapper.eq(cart.getSetmealId() != null, Cart::getSetmealId, cart.getSetmealId());
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());
        Cart cart1 = cartMapper.selectOne(wrapper);
        //number是否为0
        if (cart1 != null) {
            cart1.setNumber(cart1.getNumber() - 1);
            Integer num = cart1.getNumber();
            if (num > 0) {
                cartMapper.updateById(cart1);
            } else {
                cartMapper.deleteById(cart1.getId());

            }
        }
    }

    //清空购物车
    @Override
    public void clean() {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());
        cartMapper.delete(wrapper);

    }
}

