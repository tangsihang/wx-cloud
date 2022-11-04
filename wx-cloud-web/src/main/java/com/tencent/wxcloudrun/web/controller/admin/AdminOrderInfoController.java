
package com.tencent.wxcloudrun.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.OrderDetailParam;
import com.tencent.wxcloudrun.common.request.OrderPageParam;
import com.tencent.wxcloudrun.common.request.WxUserBaseParam;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.web.service.AdminOrderInfoService;
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
 * @date 2022/11/04
 */
@Api(tags = "订单管理-后台模块")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminOrderInfoController {

    @Autowired
    private AdminOrderInfoService orderInfoService;

    @ApiOperation("订单查询-分页")
    @PostMapping("/v1/order/page")
    @ApiRequest
    public Result<PageDTO<AdsOrderEntity>> page(@RequestBody @Validated OrderPageParam param) {
        PageDTO<AdsOrderEntity> pageResult = orderInfoService.page(param);
        return Result.Success(pageResult);
    }

    @ApiOperation("订单查询-详情")
    @PostMapping("/v1/order/detail")
    @ApiRequest
    public Result<JSONObject> detail(@RequestBody @Validated OrderDetailParam param) {
        JSONObject respJson = orderInfoService.detail(param);
        return Result.Success(respJson);
    }

    @ApiOperation("订单查询-关联用户")
    @PostMapping("/v1/order/link-user-info")
    @ApiRequest
    public Result<UserInfoResult> linUserInfo(@RequestBody @Validated WxUserBaseParam param) {
        UserInfoResult result = orderInfoService.linkUserInfo(param);
        return Result.Success(result);
    }


    @ApiOperation("订单查询-导出")
    @PostMapping("/v1/order/export")
    @ApiRequest
    public Result<Void> export(@RequestBody @Validated OrderPageParam param) {
        return Result.Success();
    }

}
