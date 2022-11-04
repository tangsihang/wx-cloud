package com.tencent.wxcloudrun.web.service;

import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.InviteDetailParam;
import com.tencent.wxcloudrun.common.request.PageBaseParam;
import com.tencent.wxcloudrun.common.response.InviteRelateResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/04
 */
public interface AdminUserInfoService {

    void login(AdminUserLoginParam param);

    PageDTO<UserInfoResult> page(PageBaseParam param);

    List<InviteRelateResult> inviteDetail(InviteDetailParam param);

}
