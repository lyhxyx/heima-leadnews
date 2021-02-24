package com.heima.common.exception;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
//@ControllerAdvice

@RestControllerAdvice   //一个相当于@ControllerAdvice+@ResponseBody
public class ExceptionCatch {

    /**
     * 捕获不可预见异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    //@ResponseBody
    public ResponseResult exception(Exception e){
        //将捕获的异常打印出来
        e.printStackTrace();

        return  ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"系统正忙.......");
    }


    /**
     * 捕获可预见异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseResult exception(CustomException e){
        //将捕获的异常打印出来
        e.printStackTrace();
        return  ResponseResult.errorResult(e.getAppHttpCodeEnum());
    }

}
