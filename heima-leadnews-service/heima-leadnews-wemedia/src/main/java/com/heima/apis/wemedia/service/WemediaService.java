package com.heima.apis.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;

public interface WemediaService extends IService<WmUser> {
    /**
     * 保存自媒体用户
     * @param wmUser
     * @return
     */
    ResponseResult insert(WmUser wmUser);

    /**
     * 查找自媒体用户
     * @param name
     * @return
     */
    WmUser findByName(String name);
}
