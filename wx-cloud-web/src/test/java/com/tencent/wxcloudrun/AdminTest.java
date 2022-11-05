package com.tencent.wxcloudrun;

import com.alibaba.fastjson.JSON;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.BaseInviteCodeParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.web.WxCloudRunApplication;
import com.tencent.wxcloudrun.web.service.AdminOrderInfoService;
import com.tencent.wxcloudrun.web.service.AdminUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WxCloudRunApplication.class})
public class AdminTest {

    @Autowired
    private AdminUserInfoService adminUserInfoService;
    @Autowired
    private AdminOrderInfoService adminOrderInfoService;


    @Test
    public void test_common_admin_check() {
        AdminUserLoginParam param = AdminUserLoginParam.builder().username("admin").password("a9527").build();
        adminUserInfoService.login(param);
    }


    @Test
    public void test_query_wx_order_page() {
        int pageNo = 0;
        int pageSize = 20;
        BasePageParam param = BasePageParam.builder().pageNo(pageNo).pageSize(pageSize).build();
        PageDTO<AdminOrderResult> pageDTO = adminOrderInfoService.page(param);
        log.info("{}", JSON.toJSONString(pageDTO));
    }

    @Test
    public void test_query_invite_user_detail() {
        BaseInviteCodeParam param = new BaseInviteCodeParam();
        param.setInviteCode("trlcgt");
        List<InviteUserDetailResult> result = adminUserInfoService.inviteUserDetail(param);
        log.info("{}", JSON.toJSONString(result));
    }
}
