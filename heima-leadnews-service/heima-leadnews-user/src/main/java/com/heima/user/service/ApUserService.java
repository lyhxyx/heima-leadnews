package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;

public interface ApUserService extends IService<ApUser> {

    /**
     * APP用户登录接口
     * @param dto
     * @return
     */
    ResponseResult login(LoginDto dto);
}
