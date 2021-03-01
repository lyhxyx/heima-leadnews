package com.heima.admin.controller.v1;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDtos;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.controller.v1
 * @Description: 用户登录
 * @Author: 李昊阳
 * @CreateDate: 2021/2/28 0:45
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@RestController
@RequestMapping("login")
@Api(value = "用户登录",tags = "login",description = "用户登录api")
public class AdUserController {
    @Autowired
    private AdUserService adUserService;
    @PostMapping("in")
    @ApiOperation("用户登录")
    public ResponseResult login(@RequestBody AdUserDtos adUserDtos){
        return adUserService.login(adUserDtos);
    }

}
