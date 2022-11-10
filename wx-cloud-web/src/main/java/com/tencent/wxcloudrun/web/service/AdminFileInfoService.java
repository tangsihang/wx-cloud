package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.request.UploadParam;

import java.io.InputStream;

/**
 * @author tangsh
 * @date 2022/11/09
 */
public interface AdminFileInfoService {

    JSONObject uploadFile(UploadParam param);

    String upload(UploadParam param, InputStream in);
}
