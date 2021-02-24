package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {

    @Override
    public ResponseResult login(AdUserDto dto) {
        //1.判断参数是否为空
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断用户名和密码是否为空
        if(StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        //3.根据用户名查询用户判断是否存在
        AdUser user = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, dto.getName()));
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }

        //4.对比密码得到结果，如果登陆成功响应TOKEN值，如果登陆失败响应密码错误
        String textPwd = dto.getPassword();//用户输入的明文密码
        String salt = user.getSalt();//随机数（盐）
        String pwd = textPwd + salt; //将明文密码与盐拼接
        //对拼接的密码进行MD5加密
        String pwdEncrypt = DigestUtils.md5DigestAsHex(pwd.getBytes());
        String pwdDB = user.getPassword();//数据库表中该用户的密文密码
        if(pwdEncrypt.equals(pwdDB)){
            //根据用户id为登录成功的用户生成token字符串
            String token = AppJwtUtil.getToken(user.getId().longValue());
            Map userMap = new HashMap<>();
            userMap.put("token",token);
            user.setPassword("");
            user.setSalt("");
            userMap.put("user",user);
            return ResponseResult.okResult(userMap);//将map返回去
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
    }
}
