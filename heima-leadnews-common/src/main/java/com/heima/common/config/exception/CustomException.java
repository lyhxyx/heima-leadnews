package com.heima.common.config.exception;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.common.config.exception
 * @Description: 自定义异常处理
 * @Author: 李昊阳
 * @CreateDate: 2021/2/24 19:52
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class CustomException extends RuntimeException{
    private AppHttpCodeEnum appHttpCodeEnum;
    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum=appHttpCodeEnum;
    }
    public AppHttpCodeEnum getAppHttpCodeEnum(){
        return appHttpCodeEnum;
    }
}
