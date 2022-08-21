package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Order;

import java.util.Date;

public interface OrderService {
    //提交订单
    void submit(Order orderparam);

    //订单查询
    Page<Order> findByPage(Integer pageNum, Integer pageSize);

    //订单明细
    Page<Order> findOrderList(Integer pageNum, Integer pageSize, String number, Date beginTime, Date endTime);
}
