
package com.tencent.wxcloudrun.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.AdminOrderPageParam;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.web.service.AdminOrderInfoService;
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
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

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
    public Result<PageDTO<AdminOrderResult>> page(@RequestBody @Validated AdminOrderPageParam param) {
        return Result.Success(orderInfoService.page(param));
    }

    @ApiOperation("订单查询-导出")
    @PostMapping("/v1/order/export")
    public void export(@RequestBody @Validated AdminOrderPageParam param, HttpServletResponse response) {
        HSSFWorkbook wb = orderInfoService.export(param);
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + "order_file.xls");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            OutputStream os = response.getOutputStream();
            os.write(bos.toByteArray());
            os.flush();
            os.close();
            bos.flush();
            bos.close();
        } catch (Exception e) {
            log.error("导出异常!", e);
            throw new BizException(ErrorCode.BIZ_BREAK, "导出文件异常!");
        }
    }

    @ApiOperation("订单查询-详情")
    @PostMapping("/v1/order/detail")
    @ApiRequest
    public Result<JSONObject> detail(@RequestBody @Validated BaseOrderNoParam param) {
        JSONObject respJson = orderInfoService.detail(param);
        return Result.Success(respJson);
    }

    @ApiOperation("订单查询-关联用户")
    @PostMapping("/v1/order/link-user-info")
    @ApiRequest
    public Result<UserInfoResult> linUserInfo(@RequestBody @Validated BaseWxUserParam param) {
        UserInfoResult result = orderInfoService.linkUserInfo(param);
        return Result.Success(result);
    }
}
