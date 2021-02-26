package com.heima.apis.article;

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
}