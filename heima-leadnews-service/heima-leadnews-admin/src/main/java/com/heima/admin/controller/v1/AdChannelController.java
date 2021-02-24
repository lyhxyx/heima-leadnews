package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelService;
import com.heima.common.config.exception.CustomException;
import com.heima.model.admin.dtos.AdChannelDtos;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.controller.v1
 * @Description:  频道管理
 * @Author: 李昊阳
 * @CreateDate: 2021/2/22 22:56
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@RestController
@RequestMapping("api/v1/channel/")
@Api(value = "频道管理",tags = "channel",description = "频道管理API")
public class AdChannelController{
    @Autowired
    private AdChannelService adChannelService;
    /**
     * @Author: Administrator
     * @Date:0:42 2021/2/23
     * @Parms [dtos]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 根据频道名称分页获取频道列表
     * @version: 1.0
     */
    @PostMapping("list")
    @ApiOperation("频道分页列表查询")
    public ResponseResult findPageByName(@RequestBody AdChannelDtos dtos){
        return adChannelService.findPageByName(dtos);
    }

    @PostMapping("save")
    @ApiOperation("新增频道")
    public ResponseResult insert(@RequestBody AdChannel adChannel) {
        return adChannelService.insert(adChannel);
    }

    @PostMapping("update")
    @ApiOperation("修改频道")
    public ResponseResult update(@RequestBody AdChannel adChannel) {
        return adChannelService.update(adChannel);
    }

    @PostMapping("del/{id}")
    @ApiOperation("删除频道")
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        if (true){
            throw new CustomException(AppHttpCodeEnum.DATA_EXIST);
        }
        return adChannelService.deleteById(id);
    }
}
