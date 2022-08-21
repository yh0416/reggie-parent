package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    //新增购物车
    @PostMapping("/cart/add")
    public ResultInfo addCart(@RequestBody Cart cart) {
        //要把添加的number返回前端,他是自己抓取的,所以返回cart
        Cart cart1 = cartService.addCart(cart);
        return ResultInfo.success(cart1);
    }

    //查询购物车
    @GetMapping("/cart/list")
    public ResultInfo finCart() {
        List<Cart> cartList = cartService.finCart();
        return ResultInfo.success(cartList);
    }

    //修改购物车
    @PostMapping("/cart/sub")
    public ResultInfo updateCart(@RequestBody Cart cart) {//requestbody只能修饰对象和集合
        cartService.updateCart(cart);
        return ResultInfo.success(null);
    }

    //清空购物车
    @DeleteMapping("/cart/clean")
    public ResultInfo clean(){
        cartService.clean();
        return ResultInfo.success(null);
    }
}
