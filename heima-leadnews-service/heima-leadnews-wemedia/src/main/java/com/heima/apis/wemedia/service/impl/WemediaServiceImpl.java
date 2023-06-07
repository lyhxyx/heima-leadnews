package com.heima.apis.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.wemedia.mapper.WemediaMapper;
import com.heima.apis.wemedia.service.WemediaService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
@Service
public class WemediaServiceImpl extends ServiceImpl<WemediaMapper, WmUser> implements WemediaService {

    @Override
    public ResponseResult insert(WmUser wmUser) {
        //1.判断参数
        if (wmUser==null){
           return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //保存自媒体用户
        save(wmUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public WmUser findByName(String name) {
        //1.判断参数
        if (StringUtils.isBlank(name)){
            return null;
        }
        //查询自媒体用户
        WmUser wmUser = getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, name));
        return wmUser;
    }
}


