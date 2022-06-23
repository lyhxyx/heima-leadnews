package com.heima.admin;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @ProjectName: heima-leadnews-371
 * @Package: com.heima.admin
 * @Description:频道管理启动类
 * @Author: 李昊阳
 * @CreateDate: 2021/2/22 23:02
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.heima.admin.mapper")
public class AdChannelApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdChannelApplication.class,args);
    }
  /**
   * @Author: Administrator
   * @Date:23:05 2021/2/22
   * @Parms []
   * @ReturnType: com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
   * @Description: mabatis-plus分页插件
   * @version: 1.0
   */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
