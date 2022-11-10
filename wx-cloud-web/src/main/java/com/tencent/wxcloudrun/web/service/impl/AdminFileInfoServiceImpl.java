package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.constants.AppConstant;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.UploadParam;
import com.tencent.wxcloudrun.web.client.WxFileClient;
import com.tencent.wxcloudrun.web.service.AdminFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author tangsh
 * @date 2022/11/09
 */
@Service
@Slf4j
public class AdminFileInfoServiceImpl implements AdminFileInfoService {

    @Autowired
    private WxFileClient wxFileClient;

    @Override
    public JSONObject uploadFile(UploadParam param) {
        JSONObject reqJson = new JSONObject();
        reqJson.put("env", AppConstant.WX_ENV_ID);
        reqJson.put("path", param.getPath());
        return wxFileClient.upload(reqJson);
    }

    @Override
    public void upload(JSONObject respJson) {
        String url = respJson.getString("url");
        String fileId = respJson.getString("file_id");
        byte[] bytes = readFromByteFile("/Users/tangsh/Downloads/mysql-dat/smb_data.pdf");
        JSONObject req = new JSONObject();
        req.put("key", respJson.getString("key"));
        req.put("Signature", respJson.getString("authorization"));
        req.put("x-cos-security-token", respJson.getString("token"));
        req.put("x-cos-meta-fileid", respJson.getString("cos_file_id"));
        req.put("file", bytes);
        log.info("url:{}", url);
        wxFileClient.uploadFile(url, req);
    }

    public byte[] readFromByteFile(String pathname) {
        try {
            File filename = new File(pathname);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new BizException(ErrorCode.BIZ_BREAK, "文件读取异常!");
        }

    }

}
