
package com.tencent.wxcloudrun.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.web.service.AdminAdsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangsh
 * @date 2022/10/27
 */
@Api(tags = "广告后台模块")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminAdsInfoController {

    @Autowired
    private AdminAdsInfoService adsInfoService;

    @ApiOperation("广告后台-分页")
    @PostMapping("/v1/ads/page")
    @ApiRequest
    public Result<PageDTO<AdsInfoEntity>> page(@RequestBody @Validated AdsPageParam param) {
        PageDTO<AdsInfoEntity> pageResult = adsInfoService.page(param);
        return Result.Success(pageResult);
    }

    @ApiOperation("广告后台-详情")
    @PostMapping("/v1/ads/detail")
    @ApiRequest
    public Result<AdsInfoEntity> detail(@RequestBody @Validated AdsBaseParam param) {
        return Result.Success(adsInfoService.detail(param));
    }

    @ApiOperation("广告后台-上架")
    @PostMapping("/v1/ads/status-on")
    @ApiRequest
    public Result<Void> statusOn(@RequestBody @Validated AdsBaseParam param) {
        adsInfoService.on(param);
        return Result.Success();
    }

    @ApiOperation("广告后台-下架")
    @PostMapping("/v1/ads/status-off")
    @ApiRequest
    public Result<Void> statusOff(@RequestBody @Validated AdsBaseParam param) {
        adsInfoService.off(param);
        return Result.Success();
    }

    @ApiOperation("广告后台-创建")
    @PostMapping("/v1/ads/create")
    @ApiRequest
    public Result<Void> edit(@RequestBody @Validated AdsCreateParam param) {
        adsInfoService.create(param);
        return Result.Success();
    }

    @ApiOperation("广告后台-编辑")
    @PostMapping("/v1/ads/edit")
    @ApiRequest
    public Result<Void> edit(@RequestBody @Validated AdsEditParam param) {
        adsInfoService.edit(param);
        return Result.Success();
    }


    @ApiOperation("广告后台-文件路径获取")
    @PostMapping("/v1/ads/get-upload-url")
    @ApiRequest
    public Result<JSONObject> getUploadUrl(@RequestBody @Validated UploadParam param) {
        JSONObject reqJson = adsInfoService.upload(param);
        return Result.Success(reqJson);
    }

    @ApiOperation("广告后台-文件上传")
    @PostMapping("/v1/ads/upload")
    @ApiRequest
    public Result<Void> uploadCallback(@RequestHeader("x-wx-openid") String openid,
                                       @RequestParam("file") String file,
                                       @RequestParam("key") String key,
                                       @RequestParam("Signature") String signature,
                                       @RequestParam("x-cos-security-token") String xCosSecurityToken,
                                       @RequestParam("x-cos-meta-fileid") String xCosMetaileid) {
        log.info("openid:{},x-cos-meta-fileid:{}", openid, xCosMetaileid);
        return Result.Success();
    }
}
