package com.heima.model.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.model.admin.dtos
 * @Description: 敏感词设置
 * @Author: 李昊阳
 * @CreateDate: 2021/2/25 21:20
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Data
public class AdSensitiveDtos extends PageRequestDto {
    //敏感词名称
    private String name;
}
