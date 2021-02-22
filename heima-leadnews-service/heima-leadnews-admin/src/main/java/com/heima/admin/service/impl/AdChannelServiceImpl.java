package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {


    @Override
    public ResponseResult findByNameAndPage(ChannelDto dto) {
        //1.判断参数是否为空
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        //2.判断处理分页参数
        dto.checkParam();

        //3.判断频道名称是否有值，如果有值就模糊查询
        LambdaQueryWrapper<AdChannel> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(dto.getName())){
            wrapper.like(AdChannel::getName,dto.getName());
        }

        //4.分页查询
        IPage<AdChannel> ipage = new Page<>(dto.getPage(),dto.getSize());
        IPage<AdChannel> page = page(ipage, wrapper);//执行分页查询，得到分页查询结果

        //5.响应数据
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }


    @Override
    public ResponseResult insert(AdChannel adChannel) {
        //1.判断参数是否为空
        if(adChannel==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.添加必要字段的值
        adChannel.setIsDefault(true);
        adChannel.setCreatedTime(new Date());

        //3.执行添加
        save(adChannel);

        //4.响应数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    @Override
    public ResponseResult update(AdChannel adChannel) {
        //1.判断参数是否为空
        if(adChannel==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.查询数据是否存在
        AdChannel channel = getById(adChannel.getId());
        if(channel==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //3.执行更新
        updateById(adChannel);

        //4.响应数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    @Override
    public ResponseResult deleteById(Integer id) {
        //1.判断参数是否为空为0
        if(id==null || id==0){
            return  ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断数据是否存在
        AdChannel channel = getById(id);
        if(channel==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"不存在的数据不能删除");
        }

        //3.判断数据是否已启用
        if(channel.getStatus()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"数据已启用不能删除");
        }

        //4.执行删除
        removeById(id);

        //5.响应数据
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
