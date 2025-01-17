
package com.tencent.wxcloudrun.web.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.web.service.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangsh
 * @date 2022/10/27
 */
@Api(tags = "订单模块")
@RestController
@RequestMapping("/front")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("订单查询-分页")
    @PostMapping("/v1/order/page")
    @ApiRequest
    public Result<PageDTO<AdminOrderResult>> page(@RequestHeader("x-wx-openid") String openid, @RequestBody @Validated BasePageParam param) {
        return Result.Success(orderInfoService.page(openid, param));
    }

    @ApiOperation("订单查询-详情")
    @PostMapping("/v1/order/detail")
    @ApiRequest
    public Result<JSONObject> detail(@RequestBody @Validated BaseOrderNoParam param) {
        return Result.Success(orderInfoService.detail(param));
    }
}
