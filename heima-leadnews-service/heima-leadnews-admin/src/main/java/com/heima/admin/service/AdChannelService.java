package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdChannelDtos;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.service
 * @Description:
 * @Author: 李昊阳
 * @CreateDate: 2021/2/22 23:31
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface AdChannelService extends IService<AdChannel> {
    /**
     * @Author: Administrator
     * @Date:23:36 2021/2/22
     * @Parms [adChannel]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 根据频道名称分页获取频道列表
     * @version: 1.0
     */
    ResponseResult findPageByName(AdChannelDtos dtos);
    /**
     * @Author: Administrator
     * @Date:21:03 2021/2/23
     * @Parms [adChannel]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 新增频道
     * @version: 1.0
     */
    ResponseResult insert(AdChannel adChannel);
    /**
     * @Author: Administrator
     * @Date:21:03 2021/2/23
     * @Parms [adChannel]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 修改频道
     * @version: 1.0
     */
    ResponseResult update(AdChannel adChannel);
    /**
     * @Author: Administrator
     * @Date:22:33 2021/2/23
     * @Parms [adChannel]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 删除频道
     * @version: 1.0
     */
    ResponseResult deleteById(Integer id);
}
