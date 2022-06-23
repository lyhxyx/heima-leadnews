package com.heima.model.admin.pojos;

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
 * @Description: 敏感词实体类
 * @Author: 李昊阳
 * @CreateDate: 2021/2/25 21:10
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Data
@TableName(value = "ad_sensitive")
public class AdSensitive implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 敏感词
     */
    @TableField("sensitives")
    private String sensitives;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
