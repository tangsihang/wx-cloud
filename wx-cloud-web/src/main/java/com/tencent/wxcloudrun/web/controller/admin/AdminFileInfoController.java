package com.tencent.wxcloudrun.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.request.UploadParam;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.web.service.AdminFileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangsh
 * @date 2022/11/09
 */
@Api(tags = "文件管理-后台模块")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminFileInfoController {

    @Autowired
    private AdminFileInfoService adminFileInfoService;

    @ApiOperation("广告后台-文件路径获取")
    @PostMapping("/v1/file/upload")
    @ApiRequest
    public Result<JSONObject> uploadFile(@RequestBody @Validated UploadParam param) {
        return Result.Success(adminFileInfoService.uploadFile(param));
    }
}
