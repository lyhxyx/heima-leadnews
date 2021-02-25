package com.heima.admin.controller.v1;

import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.AdSensitiveDtos;
import com.heima.model.admin.pojo.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.controller.v1
 * @Description: 敏感词设置
 * @Author: 李昊阳
 * @CreateDate: 2021/2/26 0:08
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@RestController
@RequestMapping("api/v1/sensitive/")
@Api(value = "敏感词设置",tags = "adSensitive",description = "敏感词设置api")
public class AdSensitiveController {
    @Autowired
    private AdSensitiveService adSensitiveService;
    @PostMapping("list")
    @ApiOperation("敏感词设置分页查询")
public ResponseResult findPageByName(@RequestBody AdSensitiveDtos adSensitiveDtos){
    return adSensitiveService.findPageByName(adSensitiveDtos);
    }
    @PostMapping("save")
    @ApiOperation("新增敏感词")
    public ResponseResult insert(@RequestBody AdSensitive adSensitive){
        return adSensitiveService.insert(adSensitive);
    }
    @PostMapping("update")
    @ApiOperation("修改敏感词")
    public ResponseResult update(@RequestBody AdSensitive adSensitive){
        return adSensitiveService.update(adSensitive);
    }
    @DeleteMapping("del/{id}")
    @ApiOperation("删除敏感词")
    public ResponseResult deleteById(@PathVariable("id") Integer id){
        return adSensitiveService.deleteById(id);
    }
}
