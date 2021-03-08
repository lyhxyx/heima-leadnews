package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 保存或更改APP文章相关表
     * @param articleDto
     * @return
     */
    ResponseResult saveArticle(ArticleDto articleDto);

    /**
     * APP首页加载文章列表
     * @param dto
     * @param type 1表示加载更多 2示加载更新
     * @return
     */
    ResponseResult load(ArticleHomeDto dto,short type);

    /**
     * 查看文章详情
     * @param dto
     * @return
     */
    ResponseResult loadInfo(ArticleInfoDto dto);
}