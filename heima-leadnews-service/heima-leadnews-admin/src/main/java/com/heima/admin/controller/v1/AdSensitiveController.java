package com.heima.admin.controller.v1;

import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController {

    @Autowired
    private AdSensitiveService adSensitiveService;

    @PostMapping("/list")
    public ResponseResult findByNameAndPage(@RequestBody SensitiveDto dto){
        return adSensitiveService.findByNameAndPage(dto);
    }

    @PostMapping("/save")
    public ResponseResult insert(@RequestBody AdSensitive adSensitive){
        return adSensitiveService.insert(adSensitive);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdSensitive adSensitive){
        return adSensitiveService.update(adSensitive);
    }
}
