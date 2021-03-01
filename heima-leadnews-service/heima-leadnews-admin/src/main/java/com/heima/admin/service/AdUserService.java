package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdUserDtos;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.service
 * @Description: 用户登录接口
 * @Author: 李昊阳
 * @CreateDate: 2021/2/26 0:41
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface AdUserService extends IService<AdUser> {
    //用户登录
    ResponseResult login(AdUserDtos adUserDtos);
}
