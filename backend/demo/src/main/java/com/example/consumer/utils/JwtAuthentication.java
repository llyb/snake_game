package com.example.consumer.utils;

import com.example.Utils.JwtUtil;
import io.jsonwebtoken.Claims;

// 用来验证
public class JwtAuthentication {
    public static int getUserId(String token) {
        int userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userid;
    }
}
