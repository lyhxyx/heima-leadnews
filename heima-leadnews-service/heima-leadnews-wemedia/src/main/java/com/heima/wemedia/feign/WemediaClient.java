package com.heima.wemedia.feign;

import com.heima.apis.wemedia.IWemediaClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
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
}
