package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);


    /**
     * 分页查询素材列表
     * @param dto
     * @return
     */
    ResponseResult list(WmMaterialDto dto);

    /**
     * 根据ID删除素材
     * @param id
     * @return
     */
    ResponseResult deleteById(Integer id);


    /**
     * 收藏或取消收藏素材
     * @param id
     * @param isCollect
     * @return
     */
    ResponseResult collect(Integer id, Short isCollect);
}
