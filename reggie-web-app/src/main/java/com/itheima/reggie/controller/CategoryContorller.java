package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryContorller {

    @Autowired
    private CategoryService categoryService;

    //首页侧边栏
    @GetMapping("/category/list")
    public ResultInfo findList() {
        List<Category> categoryList = categoryService.findAll();
        return ResultInfo.success(categoryList);
    }
}
