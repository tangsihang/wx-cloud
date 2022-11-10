package com.tencent.wxcloudrun.web.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
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
    /**
     * 过期时间 3h
     */
    private static final Integer EXPIRES_AT = 3 * 60 * 60 * 1000;

    public static String sign(String username, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRES_AT);
        //设置私钥和加密算法
        Algorithm algorithm = Algorithm.HMAC256(LOGIN_KEY);
        //设置头部信息
        Map<String, Object> header = Maps.newHashMap();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create().withHeader(header)
                .withClaim("username", username)
                .withClaim("password", password)
                .withExpiresAt(date).sign(algorithm);
    }

    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(LOGIN_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            throw new BizException(ErrorCode.TOKEN_EXPIRED, "token过期!");
        }
    }
}
