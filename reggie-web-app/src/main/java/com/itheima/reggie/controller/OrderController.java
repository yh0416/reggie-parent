package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    //提交订单
    @PostMapping("/order/submit")
    public ResultInfo submit(@RequestBody Order orderparam) {
        orderService.submit(orderparam);
        return ResultInfo.success(null);
    }

    //订单查询
    @GetMapping("/order/userPage")
    public ResultInfo findByPage(
            @RequestParam(value = "page" ,defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "5")Integer pageSize
    ){
       Page<Order> page= orderService.findByPage(pageNum,pageSize);
       return ResultInfo.success(page);
    }
}
