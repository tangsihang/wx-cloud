package com.tencent.wxcloudrun.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author tangsh
 * @date 2022/10/31
 */

public interface WebhookService {

    JSONObject respWxPayHook(JSONObject req);
}