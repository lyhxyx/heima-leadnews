package com.heima.apis.admin;

import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

public interface AdChannelControllerApi {

    /**
     * 按照频道名称分页查询频道列表
     * @param dto
     * @return
     */
    ResponseResult findByNameAndPage(ChannelDto dto);


    /**
     * 新增频道
     * @param adChannel
     * @return
     */
    ResponseResult insert(AdChannel adChannel);


    /**
     * 更新频道
     * @param adChannel
     * @return
     */
    ResponseResult update(AdChannel adChannel);
}
