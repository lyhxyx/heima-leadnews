package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojo.ApUserRealname;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.ApUserRealnameService;
import org.springframework.stereotype.Service;

@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper,ApUserRealname> implements ApUserRealnameService {
    @Override
    public ResponseResult list(AuthDto authDto) {
        //1.检查参数
        if(authDto==null){
            ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.检查分页参数
        authDto.checkParam();
        //3.检查传过来的审核参数
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper<>();
        if (authDto.getStatus()!=null){
            queryWrapper.eq(ApUserRealname::getStatus,authDto.getStatus());
        }
        //4.分页
        IPage<ApUserRealname> iPage=new Page<>(authDto.getPage(),authDto.getSize());
        IPage<ApUserRealname> page=page(iPage,queryWrapper);
        ResponseResult responseResult=new PageResponseResult(authDto.getPage(),authDto.getSize(), (int) page.getTotal());
        //5.将分页列表的数据放在data返回给前端
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}
