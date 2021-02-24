package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {

    @Override
    public ResponseResult findByNameAndPage(SensitiveDto dto) {
        //1.检查参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.检查分页参数
        dto.checkParam();

        LambdaQueryWrapper<AdSensitive> wrapper = new LambdaQueryWrapper<>();
        //3.判断敏感词是否为空，如果不为空就按照敏感词模糊查询
        if(StringUtils.isNotBlank(dto.getName())){
            wrapper.like(AdSensitive::getSensitives, dto.getName());
        }

        //4.执行分页查询
        IPage<AdSensitive> ipage = new Page<>(dto.getPage(), dto.getSize());
        IPage<AdSensitive> page = page(ipage, wrapper);

        //5.响应数据
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int)page.getTotal());
        responseResult.setData(page.getRecords());

        return responseResult;
    }


    @Override
    public ResponseResult insert(AdSensitive adSensitive) {
        //1.检查参数
        if(adSensitive==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.设置其他字段值
        adSensitive.setCreatedTime(new Date());

        //3.执行保存
        save(adSensitive);

        //4.响应数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
