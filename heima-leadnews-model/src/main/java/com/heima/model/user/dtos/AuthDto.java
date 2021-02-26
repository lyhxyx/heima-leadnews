package com.heima.model.user.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class AuthDto extends PageRequestDto {
    
    //状态
    private Short status;

    /**
     * 用户认证信息ID
     */
    private Integer id;

    /**
     * 审核驳回（审核失败）的失败原因
     */
    private String msg;
}