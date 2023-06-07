package com.heima.apis.wemedia.feign;

import com.heima.apis.wemedia.IWemediaClient;
import com.heima.apis.wemedia.service.WemediaService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自媒体微服务提供的两个feign接口  用来提供给app根据用户名查询自媒体用户  保存自媒体用户
 */
@RestController
public class WemediaClient implements IWemediaClient {

    @Autowired
    private WemediaService wemediaService;

    /**
     * 保存自媒体用户
     * @param wmUser
     * @return
     */
    @Override
    public ResponseResult save(WmUser wmUser) {
        return wemediaService.insert(wmUser);
    }

    /**
     * 根据app用户名查询自媒体用户
     * @param name
     * @return
     */
    @Override
    public WmUser findByName(String name) {
        return wemediaService.findByName(name);
    }
}
