package com.heima.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ProjectName: heima-leadnews-371-lhy
 * @Package: com.heima.admin
 * @Description:
 * @Author: 李昊阳
 * @CreateDate: 2022/4/2 12:42
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2022
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AdminGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminGatewayApplication.class,args);
    }
}
