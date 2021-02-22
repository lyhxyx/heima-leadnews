package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

public interface AdChannelService extends IService<AdChannel> {

    /**
     * 根据频道名称分页查询频道列表
     * @param dto
     * @return
     */
    ResponseResult findByNameAndPage(ChannelDto dto);
}
