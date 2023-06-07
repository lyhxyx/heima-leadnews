package com.heima.admin.gateway.filter;

import com.heima.admin.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2.是否需要鉴权，查看访问url是否是登录请求
        if(request.getURI().getPath().contains("/login/in")){
            //放行
            return chain.filter(exchange);
        }

        //3.获取token
        String token = request.getHeaders().getFirst("token");

        //4.是否存在token
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5.token是否有效
        try {
            //5.1  有效，解析token，获取用户id,重新设置到header中
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claimsBody);

            if(result == 1 || result == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            Object userId = claimsBody.get("id");
            log.info("find userId:{} from uri:{}",userId,request.getURI());
            //重新设置到header中
            ServerHttpRequest serverHttpRequest = request.mutate().headers(new Consumer<HttpHeaders>() {
                @Override
                public void accept(HttpHeaders httpHeaders) {
                    httpHeaders.add("userId", userId + "");
                }
            }).build();
            exchange.mutate().request(serverHttpRequest).build();
        }catch (Exception e){
            //5.2  无效
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();

        }
        //6.结束，放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置  值越小，优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}