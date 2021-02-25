package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.AdSensitiveDtos;
import com.heima.model.admin.pojo.AdSensitive;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.service.impl
 * @Description: 敏感词设置
 * @Author: 李昊阳
 * @CreateDate: 2021/2/25 23:18
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper,AdSensitive> implements AdSensitiveService {
    /**
     * @Author: Administrator
     * @Date:23:57 2021/2/25
     * @Parms [adSensitiveDtos]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 根据名称获取敏感词分页列表
     * @version: 1.0
     */
    @Override
    public ResponseResult findPageByName(AdSensitiveDtos adSensitiveDtos) {
        if (adSensitiveDtos==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页参数检查
        adSensitiveDtos.checkParam();
        Page page=new Page(adSensitiveDtos.getPage(),adSensitiveDtos.getSize());
        //解决硬编码
        LambdaQueryWrapper<AdSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(adSensitiveDtos.getName())){
            lambdaQueryWrapper.like(AdSensitive::getSensitives,adSensitiveDtos.getName());
        }
        IPage iPage=page(page,lambdaQueryWrapper);
        ResponseResult responseResult=new PageResponseResult(adSensitiveDtos.getPage(),adSensitiveDtos.getSize(), (int) iPage.getTotal());
        responseResult.setData(iPage.getRecords());
        return responseResult;
    }
    /**
     * @Author: Administrator
     * @Date:23:58 2021/2/25
     * @Parms [adSensitive]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 新增敏感词
     * @version: 1.0
     */
    @Override
    public ResponseResult insert(AdSensitive adSensitive) {
        if (adSensitive==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        adSensitive.setCreatedTime(new Date());
        save(adSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
    /**
     * @Author: Administrator
     * @Date:0:03 2021/2/26
     * @Parms [adSensitive]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 修改敏感词
     * @version: 1.0
     */
    @Override
    public ResponseResult update(AdSensitive adSensitive) {
        if (adSensitive==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer sensitiveId = adSensitive.getId();
        if (sensitiveId==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        updateById(adSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
    /**
     * @Author: Administrator
     * @Date:0:06 2021/2/26
     * @Parms [id]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 删除敏感词
     * @version: 1.0
     */
    @Override
    public ResponseResult deleteById(Integer id) {
        if (id==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        AdSensitive adSensitive=getById(id);
        if (adSensitive==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
