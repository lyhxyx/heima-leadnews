package com.heima.article.fegin;

import com.heima.apis.article.IArticleClient;
import com.heima.article.service.ApAuthorService;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自媒体微服务对外提供的两个fegin接口
 */
@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApAuthorService apAuthorService;
    /**
     * 根据APP的userID查询作者的接口
     * @param userId
     * @return
     */
    @Override
    public ApAuthor findByUserId(Integer userId) {
        return apAuthorService.findByUserId(userId);
    }

    /**
     * 保存作者的接口
     * @param apAuthor
     * @return
     */
    @Override
    public ResponseResult save(ApAuthor apAuthor) {

        return apAuthorService.insert(apAuthor);
    }
}
