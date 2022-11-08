package com.tencent.wxcloudrun.web.service;

import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.common.response.InvitePayDetailResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.common.response.UserPayDetailResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/04
 */
public interface AdminUserInfoService {

    String login(AdminUserLoginParam param);

    PageDTO<UserInfoResult> page(AdminUserPageParam param);

    List<InviteUserDetailResult> inviteUserDetail(BaseInviteCodeParam param);

    List<InvitePayDetailResult> invitePayDetail(BaseInviteCodeParam param);

    List<UserPayDetailResult> userPayDetail(BaseWxUserParam param);

    HSSFWorkbook export(AdminUserPageParam param);
}
