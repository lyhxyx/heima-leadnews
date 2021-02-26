package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {

    /**
     * 保存自媒体用户
     * @param wmUser
     * @return
     */
    ResponseResult insert(WmUser wmUser);

    /**
     * 根据用户名查询自媒体用户
     * @param name
     * @return
     */
    WmUser findByName(String name);
}
