package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApAuthorMapper;
import com.heima.article.service.ApAuthorService;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Service;

@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor>implements ApAuthorService {
    @Override
    public ApAuthor findByUserId(Integer userId) {
        //检查参数
        if (userId==null){
            return null;
        }
        //查询文章作者
        ApAuthor apAuthor = getOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getUserId, userId));
        return apAuthor;
    }

    @Override
    public ResponseResult insert(ApAuthor apAuthor) {
        //检查参数
        if (apAuthor==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //保存文章作者
        save(apAuthor);
        return ResponseResult.okResult(apAuthor);
    }
}
