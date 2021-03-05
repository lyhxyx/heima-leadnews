package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 保存或更改APP文章相关表
     * @param articleDto
     * @return
     */
    ResponseResult saveArticle(ArticleDto articleDto);
}