package com.heima.model.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.model.admin.dtos
 * @Description:
 * @Author: 李昊阳
 * @CreateDate: 2021/2/22 23:26
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Data
public class AdChannelDtos extends PageRequestDto {
    //频道名称
    @ApiModelProperty("频道名称")
    private String name;
}
