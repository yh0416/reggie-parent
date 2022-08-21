package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.service.AddressService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    //地址新增
    @PostMapping("/address")
    public ResultInfo addAddress(@RequestBody Address address) {
        addressService.addAddress(address);
        return ResultInfo.success(null);
    }

    //查询列表
    @GetMapping("/address/list")
    public ResultInfo findList() {
        List<Address> addressList = addressService.findList();
        return ResultInfo.success(addressList);
    }

    //修改地址-回显
    @GetMapping("/address/{id}")
    public ResultInfo updateAddress(@PathVariable("id") Long id) {

        Address address = addressService.updateAddress(id);
        return ResultInfo.success(address);
    }

    //修改地址-修改
    @PutMapping("/address")
    public ResultInfo update(@RequestBody Address address) {
        addressService.update(address);
        return ResultInfo.success(null);
    }

    //删除地址
    @DeleteMapping("/address")
    public ResultInfo delete(@RequestParam("ids") List<Long> ids) {
        addressService.delete(ids);
        return ResultInfo.success(null);
    }

    //设置地址默认值
    @PutMapping("/address/default")
    public ResultInfo SetDefaultAddress(@RequestBody Address address) {//RequestBody只能接对象和集合
        Long id = address.getId();
        addressService.SetDefaultAddress(id);
        return ResultInfo.success(null);
    }

    //查询默认地址
    @GetMapping("/address/default")
    public ResultInfo findDefaultAddress(){
       Address address= addressService.findDefaultAddress();
       if (address!=null){
           return ResultInfo.success(address);
       }else {
           return ResultInfo.error("该用户没有返回值");
       }
    }


}
