package com.heima.model.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.model.admin.pojo
 * @Description: 频道信息表
 * @Author: 李昊阳
 * @CreateDate: 2021/2/22 22:50
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Data
@TableName("ad_channel")
public class AdChannel implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道名称
     */
    @TableField("name")
    private String name;

    /**
     * 频道描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否默认频道
     * 1：默认     true
     * 0：非默认   false
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * 是否启用
     * 1：启用   true
     * 0：禁用   false
     */
    @TableField("status")
    private Boolean status;

    /**
     * 默认排序
     */
    @TableField("ord")
    private Integer ord;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

}
