package com.heima.common.config.exception;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.common.config.exception
 * @Description: 配置全局异常
 * @Author: 李昊阳
 * @CreateDate: 2021/2/24 19:43
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
//异常增强
@RestControllerAdvice
@Slf4j
public class ExceptionCatch {
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e){
        e.printStackTrace();
        //记录日志
        log.error("catch Exception:{}",e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"系统正忙..请稍后重试");
    }
    /**
     * @Author: Administrator
     * @Date:21:56 2021/2/24
     * @Parms [customException]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 捕获可预知异常
     * @version: 1.0
     */
    @ExceptionHandler(CustomException.class)
    public ResponseResult exception(CustomException customException){
        System.out.println(customException);
        log.error("catch exception:{}",customException);
        return ResponseResult.errorResult(customException.getAppHttpCodeEnum());
    }
}
