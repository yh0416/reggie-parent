package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    //地址新增
    @Override
    public void addAddress(Address address) {
        address.setUserId(UserHolder.get().getId());
        address.setIsDefault(0);
        addressMapper.insert(address);
    }

    //查询列表
    @Override
    public List<Address> findList() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());
        List<Address> addressList = addressMapper.selectList(wrapper);
        return addressList;
    }

    //修改地址
    @Override
    public Address updateAddress(Long id) {
        //回显
        Address address = addressMapper.selectById(id);
        return address;
        //修改
    }

    //修改地址-修改
    @Override
    public void update(Address address) {
        addressMapper.updateById(address);
    }

    //删除地址
    @Override
    public void delete(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            addressMapper.deleteBatchIds(ids);
        }
    }

    //设置地址默认值
    @Override
    public void SetDefaultAddress(Long id) {
        //根据user_id设置该用户所有地址为0,非默认
        //update address_book set is_default=0 where user_id={登录用户id}
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());
        Address address = new Address();
        address.setIsDefault(0);
        addressMapper.update(address, wrapper);
        //根据id,设置该地址为默认1
        Address address1 = addressMapper.selectById(id);
        address1.setIsDefault(1);
        addressMapper.updateById(address1);
    }

    //查询默认地址
    @Override
    public Address findDefaultAddress() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId())
                .eq(Address::getIsDefault, 1);
        Address address = addressMapper.selectOne(wrapper);
        return address;
    }


}
