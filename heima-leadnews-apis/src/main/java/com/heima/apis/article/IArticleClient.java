package com.heima.apis.article;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("leadnews-article")
public interface IArticleClient {

    /**
     *根据用户id查询作者信息
     * @param userId
     * @return
     */
    @GetMapping("/api/v1/author/findByUserId/{userId}")
    public ApAuthor findByUserId(@PathVariable("userId") Integer userId);

    /**
     * 保存作者
     * @param apAuthor
     * @return
     */
    @PostMapping("/api/v1/author/save")
    public ResponseResult save(@RequestBody ApAuthor apAuthor);


    /**
     * 保存或更新APP文章相关表信息
     * @param articleDto
     * @return
     */
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto articleDto);

    /**
     * 根据作者ID查询作者
     * @param id
     * @return
     */
    @GetMapping("/api/v1/author/{id}")
    public ApAuthor findOne(@PathVariable("id") Integer id);
}