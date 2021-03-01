package com.heima.model.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.model.admin.dtos
 * @Description: 用户登录
 * @Author: 李昊阳
 * @CreateDate: 2021/2/26 0:38
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Data
public class AdUserDtos extends PageRequestDto {
    //登录用户名
    private String name;
    //登录密码
    private String password;
}
