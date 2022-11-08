package com.tencent.wxcloudrun.web.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangsh
 * @date 2022/11/08
 */
@Slf4j
public class SignUtils {

    private static final String LOGIN_KEY = "sadfsfsf3r3r545dfdfasas";
    private static final Integer EXPIRES_AT = 15 * 60 * 1000;

    public static String sign(String username, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRES_AT);
        //设置私钥和加密算法
        Algorithm algorithm = Algorithm.HMAC256(LOGIN_KEY);
        //设置头部信息
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        String token = JWT.create().withHeader(header).withClaim("username", username)
                .withClaim("password", password).withExpiresAt(date).sign(algorithm);
        return token;
    }

    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(LOGIN_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.warn("JWT校验失败!:{}", e.getMessage());
            return false;
        }
    }
}
