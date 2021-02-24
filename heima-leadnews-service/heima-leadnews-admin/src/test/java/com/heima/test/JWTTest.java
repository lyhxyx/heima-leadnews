package com.heima.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * jwt生成与解析介绍
 */
public class JWTTest {


    /**
     * 生成JWT（一般在登录成功后将生成的JWT当做TOKEN来使用）
      */
    @Test
    public void testCreateJwt(){
        //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjZTAyNzQ5NC1hZjc4LTQ1MTMtODY2Ni0xYjQxMTNiZjc4ZWQiLCJzdWIiOiJhbGwiLCJ1c2VybmFtZSI6InpoYW5nc2FuIiwidXNlcklkIjoxMjMsImlhdCI6MTYxNDE0OTg3NSwiZXhwIjoxNjE0MTUzNDc1fQ.AxKYDz5vHg_EnGxm8Z9zH6oDpSKQZBjDiiEsNt_aec8
        String secret = "itcast"; //对称加密密钥
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, secret)
                .setId(UUID.randomUUID().toString()) //jwt的唯一标识
                .setSubject("all")
                .claim("username", "zhangsan")
                .claim("userId",123)
                .setIssuedAt(new Date())//创建时间
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) //1小时过期
                .compact();
        System.out.println(token);
    }

    /**
     * 解析JWT（一般登陆后访问系统时需要携带TOKEN字符串，系统会使用JWT工具类校验TOKEN字符串）
     */
    @Test
    public void testParseJwt(){
        String secret = "itcast"; //对称加密密钥
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjZTAyNzQ5NC1hZjc4LTQ1MTMtODY2Ni0xYjQxMTNiZjc4ZWQiLCJzdWIiOiJhbGwiLCJ1c2VybmFtZSI6InpoYW5nc2FuIiwidXNlcklkIjoxMjMsImlhdCI6MTYxNDE0OTg3NSwiZXhwIjoxNjE0MTUzNDc1fQ.AxKYDz5vHg_EnGxm8Z9zH6oDpSKQZBjDiiEsNt_aec8";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        System.out.println("头：" + claimsJws.getHeader());
        System.out.println("载荷："   +  claimsJws.getBody());
        System.out.println("userId：" +  claimsJws.getBody().get("userId"));
        System.out.println("签名：" + claimsJws.getSignature());
    }

}
