package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserRelationDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApUserRelationService {

    ResponseResult follow(@RequestBody UserRelationDto dto);
}
