package com.heima.wemedia.feign;

import com.heima.apis.wemedia.IWemediaClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 自媒体微服务对外提供接口的controller（虽然命名为Client，但是还是controller）
 */
@RestController
public class WemediaClient implements IWemediaClient {

    @Autowired
    private WmUserService wmUserService;

    @Autowired
    private WmNewsService wmNewsService;

    @Override
    @PostMapping("/api/v1/user/save")
    public ResponseResult save(@RequestBody WmUser wmUser) {
        return wmUserService.insert(wmUser);
    }

    @Override
    @GetMapping("/api/v1/user/findByName/{name}")
    public WmUser findByName(@PathVariable("name") String name) {
        return wmUserService.findByName(name);
    }

    /**
     * 根据自媒体文章ID查询文章
     * @param id
     * @return
     */
    @GetMapping("/api/v1/news/findOne/{id}")
    @Override
    public WmNews findNewsById(@PathVariable("id") Integer id){
        return wmNewsService.getById(id);
    }

    /**
     * 更新自媒体文章
     * @param wmNews
     * @return
     */
    @PostMapping("/api/v1/news/update")
    @Override
    public ResponseResult updateWmNews(@RequestBody WmNews wmNews){
         wmNewsService.updateById(wmNews);
         return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 根据自媒体用户ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/api/v1/user/findOne/{id}")
    @Override
    public WmUser findWmUserById(@PathVariable("id") Long id){
        return wmUserService.getById(id);
    }
}
