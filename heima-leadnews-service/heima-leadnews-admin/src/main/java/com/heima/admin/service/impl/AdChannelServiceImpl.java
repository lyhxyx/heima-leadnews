package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dtos.AdChannelDtos;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.service.impl
 * @Description: 频道管理
 * @Author: 李昊阳
 * @CreateDate: 2021/2/22 23:33
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {
    /**
     * @Author: Administrator
     * @Date:20:59 2021/2/23
     * @Parms [dtos]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 根据频道名称分页获取频道列表
     * @version: 1.0
     */
    @Override
    public ResponseResult findPageByName(AdChannelDtos dtos) {
        if (dtos == null) {
            ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //分页参数检查
        dtos.checkParam();
        Page page = new Page(dtos.getPage(), dtos.getSize());
        //解决硬编码
        LambdaQueryWrapper<AdChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dtos.getName())) {
            lambdaQueryWrapper.like(AdChannel::getName, dtos.getName());
        }
        IPage iPage = page(page, lambdaQueryWrapper);
        //封装结果
        ResponseResult responseResult = new PageResponseResult(dtos.getPage(), dtos.getSize(), (int) iPage.getTotal());
        responseResult.setData(iPage.getRecords());
        return responseResult;
    }

    /**
     * @Author: Administrator
     * @Date:21:00 2021/2/23
     * @Parms [adChannel]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 新增频道
     * @version: 1.0
     */
    @Override
    public ResponseResult insert(AdChannel adChannel) {
        if (adChannel == null) {
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        adChannel.setCreatedTime(new Date());
        adChannel.setIsDefault(false);
        save(adChannel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
    /**
     * @Author: Administrator
     * @Date:21:04 2021/2/23
     * @Parms [adChannel]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 修改频道
     * @version: 1.0
     */
    @Override
    public ResponseResult update(AdChannel adChannel) {
        if (adChannel==null||adChannel.getId()==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        updateById(adChannel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
    /**
     * @Author: Administrator
     * @Date:22:46 2021/2/23
     * @Parms [id]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 删除频道
     * @version: 1.0
     */
    @Override
    public ResponseResult deleteById(Integer id) {
        if (id==null){
            ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //判断频道是否存在  是否有效
        AdChannel channel = getById(id);
        if (channel==null){
            ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (channel.getStatus()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"数据已启用，不能删除");
        }
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
