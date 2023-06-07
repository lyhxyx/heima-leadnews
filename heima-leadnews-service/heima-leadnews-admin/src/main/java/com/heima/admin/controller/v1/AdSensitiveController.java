package com.heima.admin.controller.v1;

import com.heima.admin.service.AdSensitiveService;
import com.heima.admin.utils.BCrypt;
import com.heima.model.admin.dtos.AdSensitiveDtos;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
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
@RequestMapping("api/v1/sensitive")
@Api(value = "敏感词设置",tags = "adSensitive",description = "敏感词设置api")
public class AdSensitiveController {
    @Autowired
    private AdSensitiveService adSensitiveService;
    @PostMapping("/list")
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

class  bb{
    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex("abc".getBytes());
        System.out.println(s);
        String s2 = DigestUtils.md5DigestAsHex("abc".getBytes());
        System.out.println(s2);
        String salt = RandomStringUtils.randomAlphanumeric(10);
        String salt1= RandomStringUtils.randomAlphanumeric(10);
        String password="123"+salt;
        String s1 = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(s1);
        String password1="123"+salt1;
        String s3 = DigestUtils.md5DigestAsHex(password1.getBytes());
        System.out.println(s3);

        //String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt());
        //System.out.println(hashpw);
        boolean checkpw = BCrypt.checkpw("123456", "$2a$10$wJ6uUOptSo1npgEJwseUSuhYNQjsGrkRCvZbdvem1PHoT3oABMxFm");
        System.out.println(checkpw);
    }
}