
package com.tencent.wxcloudrun.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.common.response.AdsPageInfoResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.web.service.AdminAdsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * @author tangsh
 * @date 2022/10/27
 */
@Api(tags = "广告管理-后台模块")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminAdsInfoController {

    @Autowired
    private AdminAdsInfoService adsInfoService;

    @ApiOperation("广告后台-分页")
    @PostMapping("/v1/ads/page")
    @ApiRequest
    public Result<PageDTO<AdsPageInfoResult>> page(@RequestBody @Validated AdsPageParam param) {
        return Result.Success(adsInfoService.page(param));
    }

    @ApiOperation("广告后台-导出")
    @PostMapping("/v1/ads/export")
    @ApiRequest
    public void export(@RequestBody @Validated AdsPageParam param, HttpServletResponse response) {
        HSSFWorkbook wb = adsInfoService.export(param);
        try {
            response.setContentType("application/doc");
            response.addHeader("Content-Disposition", "attachment;filename=" + "order_file.xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出异常!", e);
            throw new BizException(ErrorCode.BIZ_BREAK, "导出文件异常!");
        }
    }

    @ApiOperation("广告后台-详情")
    @PostMapping("/v1/ads/detail")
    @ApiRequest
    public Result<AdsInfoEntity> detail(@RequestBody @Validated BaseAdsParam param) {
        return Result.Success(adsInfoService.detail(param));
    }

    @ApiOperation("广告后台-上架")
    @PostMapping("/v1/ads/status-on")
    @ApiRequest
    public Result<Void> statusOn(@RequestBody @Validated BaseAdsParam param) {
        adsInfoService.on(param);
        return Result.Success();
    }

    @ApiOperation("广告后台-下架")
    @PostMapping("/v1/ads/status-off")
    @ApiRequest
    public Result<Void> statusOff(@RequestBody @Validated BaseAdsParam param) {
        adsInfoService.off(param);
        return Result.Success();
    }


    @ApiOperation("广告后台-批量上架")
    @PostMapping("/v1/ads/batch-status-on")
    @ApiRequest
    public Result<Void> batchStatusOn(@RequestBody @Validated BatchAdsParam param) {
        adsInfoService.batchOn(param);
        return Result.Success();
    }

    @ApiOperation("广告后台-批量下架")
    @PostMapping("/v1/ads/batch-status-off")
    @ApiRequest
    public Result<Void> batchStatusOff(@RequestBody @Validated BatchAdsParam param) {
        adsInfoService.batchOff(param);
        return Result.Success();
    }

    @ApiOperation("广告后台-创建")
    @PostMapping("/v1/ads/create")
    @ApiRequest
    public Result<Void> create(@RequestBody @Validated AdsCreateParam param) {
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

    @ApiOperation("广告后台-编辑虚拟用户数")
    @PostMapping("/v1/ads/edit-virtual-num")
    @ApiRequest
    public Result<Void> updateVirtualNum(@RequestBody @Validated AdsVirtualNumEditParam param) {
        adsInfoService.updateVirtualNum(param);
        return Result.Success();
    }


    @ApiOperation("广告后台-文件路径获取")
    @PostMapping("/v1/ads/pre-upload")
    @ApiRequest
    public Result<JSONObject> getUploadUrl(@RequestBody @Validated UploadParam param) {
        JSONObject reqJson = adsInfoService.upload(param);
        return Result.Success(reqJson);
    }

    @ApiOperation("用户后台-邀请客户详情")
    @PostMapping("/v1/ads/pay-user-detail")
    @ApiRequest
    public Result<List<InviteUserDetailResult>> payUserDetail(@RequestBody @Validated BaseAdsParam param) {
        List<InviteUserDetailResult> list = adsInfoService.payUserDetail(param);
        return Result.Success(list);
    }
}
