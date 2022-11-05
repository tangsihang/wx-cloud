
package com.tencent.wxcloudrun.web.controller.admin;

import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.BaseInviteCodeParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.InvitePayDetailResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.web.service.AdminUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Api(tags = "用户管理-后台模块")
@RestController
@RequestMapping("/admin")
public class AdminUserInfoController {

    @Autowired
    private AdminUserInfoService adminUserInfoService;

    @ApiOperation("用户后台-登录")
    @PostMapping("/v1/login")
    @ApiRequest
    public Result<Void> login(@RequestBody @Validated AdminUserLoginParam param) {
        adminUserInfoService.login(param);
        return Result.Success();
    }

    @ApiOperation("用户后台-分页")
    @PostMapping("/v1/user/page")
    @ApiRequest
    public Result<PageDTO<UserInfoResult>> wxUserPage(@RequestBody @Validated BasePageParam param) {
        PageDTO<UserInfoResult> result = adminUserInfoService.page(param);
        return Result.Success(result);
    }

    @ApiOperation("用户后台-邀请客户详情")
    @PostMapping("/v1/user/invite-user-detail")
    @ApiRequest
    public Result<List<InviteUserDetailResult>> inviteUserDetail(@RequestBody @Validated BaseInviteCodeParam param) {
        List<InviteUserDetailResult> list = adminUserInfoService.inviteUserDetail(param);
        return Result.Success(list);
    }

    @ApiOperation("用户后台-邀请付费详情")
    @PostMapping("/v1/user/invite-pay-detail")
    @ApiRequest
    public Result<List<InvitePayDetailResult>> invitePayDetail(@RequestBody @Validated BaseInviteCodeParam param) {
        List<InvitePayDetailResult> list = adminUserInfoService.invitePayDetail(param);
        return Result.Success(list);
    }

    @ApiOperation("用户后台-个人下单详情")
    @PostMapping("/v1/user/user-pay-detail")
    @ApiRequest
    public Result<List<AdsOrderEntity>> userPayDetail(@RequestBody @Validated BaseWxUserParam param) {
        List<AdsOrderEntity> list = adminUserInfoService.userPayDetail(param);
        return Result.Success(list);
    }


}
