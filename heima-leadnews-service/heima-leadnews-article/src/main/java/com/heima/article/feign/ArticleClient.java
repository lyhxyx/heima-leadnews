package com.heima.article.feign;

import com.heima.apis.article.IArticleClient;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ApAuthorService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章微服务对外提供接口的controller（虽然命名为Client，但是还是controller）
 */
@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApAuthorService apAuthorService;

    @Autowired
    private ApArticleService apArticleService;

    @Override
    @GetMapping("/api/v1/author/findByUserId/{userId}")
    public ApAuthor findByUserId(@PathVariable("userId") Integer userId) {
        return apAuthorService.findByUserId(userId);
    }

    @Override
    @PostMapping("/api/v1/author/save")
    public ResponseResult save(@RequestBody ApAuthor apAuthor) {
        return apAuthorService.insert(apAuthor);
    }


    @Override
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto articleDto){
        return apArticleService.saveArticle(articleDto);
    }

    @Override
    @GetMapping("/api/v1/author/{id}")
    public ApAuthor findOne(@PathVariable("id") Integer id){
        return apAuthorService.getById(id);
    }
}
