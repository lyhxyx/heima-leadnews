package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojo.ApUserRealname;

public interface ApUserRealnameService extends IService<ApUserRealname> {

    /**
     * 认证信息分页查询
     * @param authDto
     * @return
     */
    ResponseResult list(AuthDto authDto);
}
