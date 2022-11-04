
package com.tencent.wxcloudrun.web.controller.admin;

import com.tencent.wxcloudrun.common.annotation.ApiRequest;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.InviteDetailParam;
import com.tencent.wxcloudrun.common.request.PageBaseParam;
import com.tencent.wxcloudrun.common.response.InviteRelateResult;
import com.tencent.wxcloudrun.common.response.Result;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.UserEntity;
import com.tencent.wxcloudrun.dao.entity.UserInviteRelateEntity;
import com.tencent.wxcloudrun.web.service.AdminUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public Result<PageDTO<UserInfoResult>> wxUserPage(@RequestBody @Validated PageBaseParam param) {
        PageDTO<UserInfoResult> result = adminUserInfoService.page(param);
        return Result.Success(result);
    }

    @ApiOperation("用户后台-邀请详情")
    @PostMapping("/v1/user/invite-detail")
    @ApiRequest
    public Result<List<InviteRelateResult>> inviteDetail(@RequestBody @Validated InviteDetailParam param) {
        List<InviteRelateResult> list = adminUserInfoService.inviteDetail(param);
        return Result.Success(list);
    }


}
