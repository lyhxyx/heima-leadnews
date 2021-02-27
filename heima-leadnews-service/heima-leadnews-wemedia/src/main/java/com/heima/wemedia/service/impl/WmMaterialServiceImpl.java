package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {


    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.oss.prefix}")
    private String prefix; //oss上资源存储的顶层目录

    @Value("${file.oss.web-site}")
    private String webSite; //访问oos的域名

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.检查上传的文件是否为空
        if(multipartFile==null || multipartFile.isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"上传的文件不能为空");
        }

        //2.检查当前用户是否已登录
        WmUser user = WmThreadLocalUtils.getUser();
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.处理上传文件的文件名，保证文件名是唯一的
        //处理文件名前缀，文件名不重复
        String fileNamePrefix = UUID.randomUUID().toString().replace("-","");
        //获取上传的文件的原始文件名 11.1.11.png
        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");//得到最后一个.的索引位置
        String fileNamePostfix = originalFilename.substring(index);//文件名后缀
        //将文件名前缀与后缀拼接在一起
        String fullName = fileNamePrefix + fileNamePostfix;

        //4.执行上传文件到OSS上
        String fileUrl = "";
        try {
            //上传之后响应的fileUrl的值格式：/material/a/b/d/11.png
            fileUrl = fileStorageService.store(prefix, fullName, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"上传出现问题");
        }

        //5.将素材数据保存到素材表
        WmMaterial material = new WmMaterial();
        material.setUrl(fileUrl);
        material.setUserId(user.getId());
        material.setType((short)0); // 0:图片 ， 1：视频
        material.setIsCollection((short)0); //0:未收藏  ， 1：已收藏
        material.setCreatedTime(new Date());
        save(material);

        //6.响应数据，需要让素材在上传完成后在页面能够显示
        material.setUrl(webSite + material.getUrl());

        return ResponseResult.okResult(material);
    }


    @Override
    public ResponseResult list(WmMaterialDto dto) {
        //1.检查参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断用户是否已登录
        WmUser user = WmThreadLocalUtils.getUser();
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.判断收藏字段是否有值，如果有直接按照收藏字段查询
        LambdaQueryWrapper<WmMaterial> wrapper = new LambdaQueryWrapper();
        if(dto.getIsCollection()!=null){
            wrapper.eq(WmMaterial::getIsCollection, dto.getIsCollection());
        }

        //4.分页查询条件加上当前用户 并且 素材列表应该根据时间倒排
        wrapper.eq(WmMaterial::getUserId, user.getId());
        wrapper.orderByDesc(WmMaterial::getCreatedTime);

        //5.响应数据，并让图片数据能在页面显示
        IPage<WmMaterial> iPage = new Page<>(dto.getPage(), dto.getSize());
        IPage<WmMaterial> page = page(iPage, wrapper);

        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(),(int)page.getTotal());
        List<WmMaterial> wmMaterialList = page.getRecords();
        for (WmMaterial wmMaterial : wmMaterialList) {
            wmMaterial.setUrl(webSite +  wmMaterial.getUrl());
        }
        responseResult.setData(wmMaterialList);
        return responseResult;
    }
}
