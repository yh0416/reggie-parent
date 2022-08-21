package com.itheima.reggie.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

//JWT的生成和解析
public class JwtUtil {
    // 创建token
    public static String createToken(Map claims) {
        return Jwts.builder()
                .setClaims(claims) //设置响应数据体
                .signWith(SignatureAlgorithm.HS256, "reggie") //设置加密方法和加密盐
                .compact();
    }

    // 解析token
    public static Map parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey("reggie")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}