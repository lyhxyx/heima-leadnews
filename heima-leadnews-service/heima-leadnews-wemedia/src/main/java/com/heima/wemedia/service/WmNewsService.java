package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 文章分页列表查询
     * @param dto
     * @return
     */
    ResponseResult list(WmNewsPageReqDto dto);

    /**
     * 提交审核或保存草稿
     * @param dto
     * @param isSumit 0-表示保存草稿， 1-提交审核
     * @return
     */
    ResponseResult submit(WmNewsDto dto, Short isSumit);

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    ResponseResult findOne(Integer id);

    /**
     * 删除文章
     * @param id
     * @return
     */
    ResponseResult delOne(Integer id);


    /**
     * 上下架
     * @param dto
     * @return
     */
    ResponseResult downOrUp(WmNewsDto dto);


    /**
     * 分页查询自媒体文章列表
     * @param dto
     * @return
     */
    ResponseResult findList(NewsAuthDto dto);

    /**
     * 根据文章ID查询文章详情vo
     * @param id
     * @return
     */
    ResponseResult findWmNewsVo(Integer id);

    /**
     * 人工审核自媒体文章
     * @param dto
     * @param status
     * @return
     */
    ResponseResult auth(NewsAuthDto dto, Short status);
}
