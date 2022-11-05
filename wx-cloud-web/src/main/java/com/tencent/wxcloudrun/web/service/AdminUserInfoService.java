package com.tencent.wxcloudrun.web.service;

import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.BaseInviteCodeParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.InvitePayDetailResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/04
 */
public interface AdminUserInfoService {

    void login(AdminUserLoginParam param);

    PageDTO<UserInfoResult> page(BasePageParam param);

    List<InviteUserDetailResult> inviteUserDetail(BaseInviteCodeParam param);

    List<InvitePayDetailResult> invitePayDetail(BaseInviteCodeParam param);

    List<AdsOrderEntity> userPayDetail(BaseWxUserParam param);

}
