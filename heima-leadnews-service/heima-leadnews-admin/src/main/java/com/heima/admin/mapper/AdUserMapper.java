package com.heima.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.admin.pojos.AdUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin.mapper
 * @Description: 用户登录接口
 * @Author: 李昊阳
 * @CreateDate: 2021/2/26 0:40
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Mapper
public interface AdUserMapper extends BaseMapper<AdUser> {
}
