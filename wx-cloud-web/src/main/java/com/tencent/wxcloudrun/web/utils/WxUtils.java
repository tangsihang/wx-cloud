package com.tencent.wxcloudrun.web.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tangsh
 * @date 2022/10/30
 */

@Data
@Slf4j
public class WxUtils {

    public static JSONObject getData(String response) {
        JSONObject respJson = JSON.parseObject(response);
        if (respJson == null) {
            log.warn("请求微信-接口异常");
            throw new BizException(ErrorCode.BIZ_BREAK, "请求微信-接口异常");
        }
        Integer errorCode = respJson.getInteger("errcode");
        String errmsg = respJson.getString("errmsg");
        if (errorCode != 0) {
            throw new BizException(errorCode, errmsg);
        }
        JSONObject wxResult = respJson.getJSONObject("respdata");
        if (wxResult == null) {
            log.warn("请求微信-接口异常: {}", response);
            return respJson;
        }
        if (!"SUCCESS".equals(wxResult.getString("return_code"))) {
            log.warn("请求微信-接口异常 ,{}", wxResult.toJSONString());
            return respJson;
        }
        return wxResult;
    }

    public static JSONObject getPhoneInfo(String response) {
        JSONObject respJson = JSON.parseObject(response);
        if (respJson == null) {
            log.warn("请求微信-接口异常");
            throw new BizException(ErrorCode.BIZ_BREAK, "请求微信-接口异常");
        }
        Integer errorCode = respJson.getInteger("errcode");
        String errmsg = respJson.getString("errmsg");
        if (errorCode != 0) {
            throw new BizException(errorCode, errmsg);
        }
        JSONObject wxResult = respJson.getJSONObject("phone_info");
        if (wxResult == null) {
            log.warn("请求微信-接口异常: {}", response);
            return respJson;
        }
        return wxResult;
    }

    public static JSONObject getFileInfo(String response) {
        JSONObject respJson = JSON.parseObject(response);
        if (respJson == null) {
            log.warn("请求微信-接口异常");
            throw new BizException(ErrorCode.BIZ_BREAK, "请求微信-接口异常");
        }
        Integer errorCode = respJson.getInteger("errcode");
        String errmsg = respJson.getString("errmsg");
        if (errorCode != 0) {
            throw new BizException(errorCode, errmsg);
        }
        return respJson;
    }

    public static JSONObject respSuccess() {
        JSONObject resp = new JSONObject();
        resp.put("errcode", 0);
        resp.put("errmsg", "OK");
        return resp;
    }

    public static JSONObject respFail() {
        JSONObject resp = new JSONObject();
        resp.put("errcode", -1);
        resp.put("errmsg", "ERROR");
        return resp;
    }
}
