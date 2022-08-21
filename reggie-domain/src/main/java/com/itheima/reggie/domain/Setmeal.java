package com.itheima.reggie.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class Setmeal implements Serializable {
    private Long id;//主键

    private Long categoryId;//分类id

    private String name;//套餐名称

    private BigDecimal price;//套餐价格

    private Integer status;//状态 0:停用 1:启用

    private String code;//编码

    private String description;//描述信息

    private String image;//图片

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "MGT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(exist = false)
    private List<SetmealDish> setmealDishes;//套餐关联的菜品集合

    @TableField(exist = false)
    private String categoryName;//菜品分类的名称

    //是否删除
    private Integer isDeleted;
}
