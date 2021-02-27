package com.heima.wemedia.controller.v1;

import com.heima.common.constants.wemedia.WemediaConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {


    @Autowired
    private WmMaterialService wmMaterialService;

    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }


    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialDto dto){
        return wmMaterialService.list(dto);
    }

    @GetMapping("/del_picture/{id}")
    public ResponseResult deleteById(@PathVariable("id") Integer id){
        return wmMaterialService.deleteById(id);
    }


    @GetMapping("/collect/{id}")
    public ResponseResult collect(@PathVariable("id") Integer id){
        return wmMaterialService.collect(id, WemediaConstants.COLLECT_MATERIAL);
    }


    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollect(@PathVariable("id") Integer id){
        return wmMaterialService.collect(id,WemediaConstants.CANCEL_COLLECT_MATERIAL);
    }
}
