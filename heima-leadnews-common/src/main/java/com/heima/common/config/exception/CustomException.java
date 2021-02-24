package com.heima.common.config.exception;

import com.heima.model.common.dtos.ResponseResult;

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
    private ResponseResult responseResult;
    public CustomException(ResponseResult responseResult){
        this.responseResult=responseResult;
    }
    public ResponseResult getResponseResult(){
        return responseResult;
    }
}
