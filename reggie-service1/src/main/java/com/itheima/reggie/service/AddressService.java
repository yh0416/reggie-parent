package com.itheima.reggie.service;

import com.itheima.reggie.domain.Address;

import java.util.List;

public interface AddressService {
    //地址新增
    void addAddress(Address address);

    //查询列表
    List<Address> findList();

    //修改地址--回显
    Address updateAddress(Long id);

    //修改地址-修改
    void update(Address address);

    //删除地址
    void delete(List<Long> ids);

    //设置地址默认值
    void SetDefaultAddress(Long id);

    //查询默认地址
    Address findDefaultAddress();
}
