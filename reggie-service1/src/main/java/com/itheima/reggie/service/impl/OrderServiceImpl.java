package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.domain.OrderDetail;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.mapper.CartMapper;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.CartService;
import com.itheima.reggie.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    //提交订单
    @Override
    public void submit(Order orderparam) {
        // 根据地址id查询地址信息
        Address address = addressMapper.selectById(orderparam.getAddressId());
        BigDecimal sum = new BigDecimal(0);
        // 收到生成一个订单id
        Long orderId = IdWorker.getId();

        //1.生成具体信息
        //1-1获取当前用户的购物车
        List<Cart> cartList = cartService.finCart();
        if (CollectionUtil.isNotEmpty(cartList)) {
            //1-2创建详情order_detail,set
            for (Cart cart : cartList) {
                OrderDetail orderDetail = new OrderDetail();
                BeanUtils.copyProperties(cart, orderDetail);//大批量,复制属性
                orderDetail.setOrderId(orderId);
                //sum+=amout*number
                sum = sum.add(cart.getAmount().multiply(BigDecimal.valueOf(cart.getNumber())));//BigDecimal的转换
                orderDetailMapper.insert(orderDetail);
            }
        }

//        2.生成订单()
        orderparam.setId(orderId);
        orderparam.setNumber(orderId + "");//Long-->String
        orderparam.setStatus(1);
        orderparam.setUserId(UserHolder.get().getId());
        orderparam.setOrderTime(new Date());
        orderparam.setCheckoutTime(new Date());
        orderparam.setAmount(sum);
        orderparam.setPhone(address.getPhone());
        orderparam.setAddress(address.getDetail());
        orderparam.setUserName(UserHolder.get().getName());
        orderparam.setConsignee(address.getConsignee());
        orderMapper.insert(orderparam);

        //3.清空购物车中的数据
        cartService.clean();
    }

    //订单查询
    @Override
    public Page<Order> findByPage(Integer pageNum, Integer pageSize) {
        //1-1设置分页条件
        Page<Order> page = new Page<>(pageNum, pageSize);
        //1-2设置业务条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, UserHolder.get().getId())
                .orderByDesc(Order::getCheckoutTime);
        page = orderMapper.selectPage(page, wrapper);
        //2.补充订单详情
        List<Order> orderList = page.getRecords();
        if (CollectionUtil.isNotEmpty(orderList)) {
            for (Order order : orderList) {
                LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(OrderDetail::getOrderId, order.getId());
                List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper1);
                order.setOrderDetails(orderDetailList);
            }
        }
        return page;
    }

    //订单明细
    @Override
    public Page<Order> findOrderList(Integer pageNum, Integer pageSize, String number, Date beginTime, Date endTime) {
        //1-1设置分页条件
        Page<Order> page = new Page<>(pageNum, pageSize);
        //1-2设置业务条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper//eq(Order::getUserId, UserHolder.get().getId())
                .like(StringUtils.isNotEmpty(number),Order::getNumber, number)
                .between(beginTime!=null,Order::getCheckoutTime, beginTime, endTime);
        page = orderMapper.selectPage(page, wrapper);
        System.out.println(page);
        return page;
    }

}
