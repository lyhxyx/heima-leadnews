package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("leadnews-wemedia")
public interface IWemediaClient {

    /**
     * 创建自媒体用户
     * @param wmUser
     * @return
     */
    @PostMapping("/api/v1/user/save")
    public ResponseResult save(@RequestBody WmUser wmUser) ;

    /**
     * 根据用户名查询自媒体用户
     * @param name
     * @return
     */
    @GetMapping("/api/v1/user/findByName/{name}")
    public WmUser findByName(@PathVariable("name") String name);


    /**
     * 根据自媒体文章ID查询文章
     * @param id
     * @return
     */
    @GetMapping("/api/v1/news/findOne/{id}")
    WmNews findNewsById(@PathVariable("id") Integer id);

    /**
     * 更新自媒体文章
     * @param wmNews
     * @return
     */
    @PostMapping("/api/v1/news/update")
    ResponseResult updateWmNews(@RequestBody WmNews wmNews);

    /**
     * 根据自媒体用户ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/api/v1/user/findOne/{id}")
    WmUser findWmUserById(@PathVariable("id") Long id);

    /**
     * 查询待发布的文章ID集合
     * @return
     */
    @GetMapping("/api/v1/news/findRelease")
    List<Integer> findRelease();
}