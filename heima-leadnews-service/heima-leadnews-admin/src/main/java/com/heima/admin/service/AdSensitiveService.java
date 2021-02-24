package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;

public interface AdSensitiveService extends IService<AdSensitive> {

    /**
     * 分页查询敏感词列表
     * @param dto
     * @return
     */
    ResponseResult findByNameAndPage(SensitiveDto dto);


    /**
     * 新增敏感词
     * @param adSensitive
     * @return
     */
    ResponseResult insert(AdSensitive adSensitive);


    /**
     * 更新敏感词
     * @param adSensitive
     * @return
     */
    ResponseResult update(AdSensitive adSensitive);

    /**
     * 根据ID删除敏感词
     * @param id
     * @return
     */
    ResponseResult deleteById(Integer id);
}
