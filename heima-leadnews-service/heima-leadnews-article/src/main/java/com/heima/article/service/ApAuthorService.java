package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;

public interface ApAuthorService extends IService<ApAuthor> {
    /**
     * 根据APP的UserId查询作者
     * @param userId
     * @return
     */
    ApAuthor findByUserId(Integer userId);

    /**
     * 保存作者
     * @param apAuthor
     * @return
     */
    ResponseResult insert(ApAuthor apAuthor);
}
