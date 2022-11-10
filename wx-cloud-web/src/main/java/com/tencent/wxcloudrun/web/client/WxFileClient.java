package com.tencent.wxcloudrun.web.client;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.tencent.wxcloudrun.web.utils.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author tangsh
 * @date 2022/11/03
 */

@Service
@Slf4j
public class WxFileClient {

    @Value("${wx.app.upload.file.url:https://api.weixin.qq.com/tcb/uploadfile}")
    public String WX_APP_UPLOAD_FILE_URL;

    public JSONObject upload(JSONObject reqJson) {
        String response = HttpRequest.post(WX_APP_UPLOAD_FILE_URL)
                .header(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_JSON)
                .send(reqJson.toJSONString())
                .body();
        log.info("请求:{},响应:{}", reqJson.toJSONString(), response);
        return WxUtils.getFileInfo(response);
    }

    public void uploadFile(String url, Map<String, String> params, InputStream in) {
        HttpRequest request = HttpRequest.post(url);
        params.forEach(request::part);
        request.part("file", in);
        request.body();
    }


}
