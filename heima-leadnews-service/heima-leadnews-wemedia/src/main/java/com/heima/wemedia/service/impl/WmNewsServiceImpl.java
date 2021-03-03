package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Value("${file.oss.web-site}")
    private String webSite;

    //select * from wm_user where user_id = 当前登陆的用户ID;
    @Override
    public ResponseResult list(WmNewsPageReqDto dto) {
        //1.检查参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断用户是否登陆
        WmUser user = WmThreadLocalUtils.getUser();
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        LambdaQueryWrapper<WmNews> wrapper = new LambdaQueryWrapper<>();
        //3.判断dto内属性值是否为空，如果不为空，就拼接查询条件
        if(dto.getStatus()!=null){ //判断请求参数中的状态值
            wrapper.eq(WmNews::getStatus, dto.getStatus());
        }

        if(StringUtils.isNotBlank(dto.getKeyword())){ //判断请求参数中的搜索关键词
            wrapper.like(WmNews::getTitle,dto.getKeyword());
        }

        if(dto.getChannelId()!=null && dto.getChannelId()>0){//判断请求参数中的频道ID
            wrapper.eq(WmNews::getChannelId, dto.getChannelId());
        }

        if(dto.getBeginPubDate()!=null && dto.getEndPubDate()!=null){//判断请求参数中的发布时间的起止时间
            wrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(), dto.getEndPubDate());
        }

        //4.查询条件添加当前登陆用户ID
        wrapper.eq(WmNews::getUserId,user.getId());

        //5.查询条件指定排序方式，按照创建时间倒排
        wrapper.orderByDesc(WmNews::getCreatedTime);

        //6.执行分页查询，得到结果
        IPage<WmNews> ipage = new Page<>(dto.getPage(), dto.getSize());
        IPage<WmNews> page = page(ipage, wrapper);

        //7.封装响应结果，设置素材的域名前缀
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setHost(webSite);
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}
