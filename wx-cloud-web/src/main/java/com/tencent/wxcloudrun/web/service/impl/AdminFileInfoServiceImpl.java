package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.common.request.UploadParam;
import com.tencent.wxcloudrun.web.client.WxFileClient;
import com.tencent.wxcloudrun.web.service.AdminFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

/**
 * @author tangsh
 * @date 2022/11/09
 */
@Service
@Slf4j
public class AdminFileInfoServiceImpl implements AdminFileInfoService {

    @Value("${wx.env.id:prod-9ge6u8sn7684a421}")
    public String WX_ENV_ID;

    @Autowired
    private WxFileClient wxFileClient;

    @Override
    public JSONObject uploadFile(UploadParam param) {
        JSONObject reqJson = new JSONObject();
        reqJson.put("env", WX_ENV_ID);
        reqJson.put("path", param.getPath());
        return wxFileClient.upload(reqJson);
    }

    @Override
    public String upload(UploadParam param, InputStream in) {
        JSONObject respJson = uploadFile(param);
        String url = respJson.getString("url");
        String fileId = respJson.getString("file_id");
        Map<String, String> params = Maps.newHashMap();
        params.put("key", param.getPath());
        params.put("Signature", respJson.getString("authorization"));
        params.put("x-cos-security-token", respJson.getString("token"));
        params.put("x-cos-meta-fileid", respJson.getString("cos_file_id"));
        wxFileClient.uploadFile(url, params, in);
        return fileId;
    }
}
