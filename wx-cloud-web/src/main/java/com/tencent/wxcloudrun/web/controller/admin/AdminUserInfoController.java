
package com.tencent.wxcloudrun.web.controller.admin;

import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Api(tags = "用户管理-后台模块")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminUserInfoController {

    @ApiOperation("用户后台-登录")
    @PostMapping("/v1/user/login")
    @ApiRequest
    public Result<Void> login(@RequestBody @Validated AdminUserLoginParam param) {
        return Result.Success();
    }

}
