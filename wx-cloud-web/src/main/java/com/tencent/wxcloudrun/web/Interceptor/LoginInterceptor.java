package com.tencent.wxcloudrun.web.Interceptor;

import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.web.utils.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tangsh
 * @date 2022/11/08
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new BizException(ErrorCode.PARAM_ERROR, "token不能为空");
        }
        boolean verify;
        try {
            verify = SignUtils.verify(token);
            return verify;
        } catch (Exception e) {
            return false;
        }
    }
}