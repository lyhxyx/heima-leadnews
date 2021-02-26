package com.heima.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.user.mapper.ApUserRealnameMapper;
import org.springframework.stereotype.Service;

@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {


    @Override
    public ResponseResult list(AuthDto dto) {
        //1.校验参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.检查分页参数
        dto.checkParam();

        LambdaQueryWrapper<ApUserRealname> wrapper = new LambdaQueryWrapper<>();
        //3.判断status参数是否为空，如果不为空，按照状态字段查询
        if(dto.getStatus()!=null){
            wrapper.eq(ApUserRealname::getStatus,dto.getStatus());
        }

        //4.分页查询
        IPage<ApUserRealname> ipage = new Page<>(dto.getPage(), dto.getSize());
        IPage<ApUserRealname> page = page(ipage, wrapper);

        //5.封装结果并响应
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}
