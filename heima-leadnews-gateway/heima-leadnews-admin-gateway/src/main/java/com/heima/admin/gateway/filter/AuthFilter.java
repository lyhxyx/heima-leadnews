package com.heima.admin.gateway.filter;

import com.heima.admin.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求对象
        ServerHttpRequest request = exchange.getRequest();

        //2.获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        //3.判断当前请求是否是登录请求（根据请求路径）
        String path = request.getURI().getPath();

        //4.如果当前用户是登陆请求，直接放行，不校验token
        if(path.contains("/login/in")){
            return chain.filter(exchange);
        }

        //5.如果当前请求是非登录请求，继续判断，从请求头中获取token
        String token = request.getHeaders().getFirst("token");

        //6.判断token是否为空，如果为空响应401
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //7.如果token非空，校验TOKEN的合法性得到结果
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claimsBody);
            if(result==-1 || result==0){
                //8.如果校验通过则请求放行
                String userId = claimsBody.get("id") + "";//从载荷中获取当前用户的id
                //将userId存入header中，方便后续业务微服务能随机取用当前登录用户的id
                request.mutate().header("userId", userId);
                return chain.filter(exchange);

            } else {
                //9.校验不通过，响应401
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //10.如果jwt解析抛出异常，响应401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0; //值越小，执行优先级越高
    }
}
