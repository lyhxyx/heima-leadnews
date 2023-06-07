package com.heima.user.controller.v1;

import com.baomidou.mybatisplus.extension.api.R;
import com.heima.common.constants.user.UserConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.user.service.ApUserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ApUserRealnameController {
    @Autowired
    ApUserRealnameService apUserRealnameService;

    /**
     * 用户认证信息分页查询
     * @param authDto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult list(@RequestBody AuthDto authDto){
        return apUserRealnameService.list(authDto);
    }

    /**
     * 用户认证审核通过
     * @param authDto
     * @return
     */
    @PostMapping("authPass")
    public ResponseResult authPass(@RequestBody AuthDto authDto){
        return apUserRealnameService.auth(authDto, UserConstants.PASS_AUTH);
    }

    /**
     * 用户认证审核不通过-驳回
     * @param authDto
     * @return
     */
    @PostMapping("authFail")
    public ResponseResult authFail(@RequestBody AuthDto authDto){
        return apUserRealnameService.auth(authDto,UserConstants.FAIL_AUTH);
    }

}
