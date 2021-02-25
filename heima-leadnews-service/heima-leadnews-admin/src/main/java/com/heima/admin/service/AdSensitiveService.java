package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdSensitiveDtos;
import com.heima.model.admin.pojo.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.service
 * @Description:
 * @Author: 李昊阳
 * @CreateDate: 2021/2/25 23:05
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface AdSensitiveService extends IService<AdSensitive> {
    /**
     * @Author: Administrator
     * @Date:23:12 2021/2/25
     * @Parms [adSensitiveDtos]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 根据敏感词名称获取敏感词列表
     * @version: 1.0
     */
    ResponseResult findPageByName(AdSensitiveDtos adSensitiveDtos);
    /**
     * @Author: Administrator
     * @Date:23:16 2021/2/25
     * @Parms [adSensitive]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 新增敏感词
     * @version: 1.0
     */
    ResponseResult insert(AdSensitive adSensitive);
    /**
     * @Author: Administrator
     * @Date:23:17 2021/2/25
     * @Parms [adSensitive]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 修改敏感词
     * @version: 1.0
     */
    ResponseResult update(AdSensitive adSensitive);
    /**
     * @Author: Administrator
     * @Date:23:17 2021/2/25
     * @Parms [id]
     * @ReturnType: com.heima.model.common.dtos.ResponseResult
     * @Description: 根据id删除敏感词
     * @version: 1.0
     */
    ResponseResult deleteById(Integer id);
}
