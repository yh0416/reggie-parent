package com.itheima.reggie.service;

import com.itheima.reggie.domain.Cart;

import java.util.List;

public interface CartService {
    //新增购物车
    Cart addCart(Cart cart);

    //查询购物车
    List<Cart> finCart();

    //修改购物车
    void updateCart(Cart cart);

    //清空购物车
    void clean();
}
