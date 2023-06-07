package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.admin.utils.AppJwtUtil;
import com.heima.model.admin.dtos.AdUserDtos;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.service.impl
 * @Description: 用户登录业务
 * @Author: 李昊阳
 * @CreateDate: 2021/2/26 0:42
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper,AdUser> implements AdUserService {
    /**
     * @Author: Administrator
     * @Date:0:44 2021/2/28
     * @Parms [adUserDtos]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 用户登录
     * @version: 1.0
     */
    @Override
    public ResponseResult login(AdUserDtos adUserDtos) {
        if (adUserDtos==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //校验参数
        if (StringUtils.isBlank(adUserDtos.getName())||StringUtils.isBlank(adUserDtos.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取用户名
        AdUser adUser=getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, adUserDtos.getName()));
        if (adUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }
        //对比校验密码  登陆成功设置tocken值  失败响应密码错误
        String password = adUserDtos.getPassword();
        String salt = adUser.getSalt();
        String passwordBCR=DigestUtils.md5DigestAsHex((password+salt).getBytes());
        String password1 = adUser.getPassword();
        //密码匹配（相同）
        if (passwordBCR.equals(password1)){
            String token = AppJwtUtil.getToken(adUser.getId().longValue());
            Map map=new HashMap();
            map.put("token", token);
            adUser.setPassword("");
            adUser.setSalt("");
            map.put("user",adUser);
            return ResponseResult.okResult(map);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
    }
}
