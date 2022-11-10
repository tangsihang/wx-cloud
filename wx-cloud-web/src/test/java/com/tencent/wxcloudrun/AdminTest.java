package com.tencent.wxcloudrun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.common.response.InvitePayDetailResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.web.WxCloudRunApplication;
import com.tencent.wxcloudrun.web.service.AdminAdsInfoService;
import com.tencent.wxcloudrun.web.service.AdminFileInfoService;
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
    @Autowired
    private AdminAdsInfoService adminAdsInfoService;
    @Autowired
    private AdminFileInfoService adminFileInfoService;


    @Test
    public void test_common_admin_check() {
        AdminUserLoginParam param = AdminUserLoginParam.builder().username("admin").password("a9527").build();
        adminUserInfoService.login(param);
    }

    @Test
    public void test_query_wx_user_page() {
        int pageNo = 1;
        int pageSize = 10;
        AdminUserPageParam param = new AdminUserPageParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        PageDTO<UserInfoResult> pageDTO = adminUserInfoService.page(param);
        log.info("{}", JSON.toJSONString(pageDTO));
    }


    @Test
    public void test_query_order_page() {
        int pageNo = 0;
        int pageSize = 20;
        AdminOrderPageParam param = new AdminOrderPageParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
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

    @Test
    public void test_query_invite_pay_detail() {
        BaseInviteCodeParam param = new BaseInviteCodeParam();
        param.setInviteCode("trlcgt");
        List<InvitePayDetailResult> result = adminUserInfoService.invitePayDetail(param);
        log.info("{}", JSON.toJSONString(result));
    }

    @Test
    public void test_update_status() {
        AdsEditParam param = new AdsEditParam();
        param.setId(16);
        param.setVirtualNum(1222);
        adminAdsInfoService.edit(param);
    }


    @Test
    public void test_upload() {
        String msg = "{\"errcode\":0,\"authorization\":\"q-sign-algorithm=sha1&q-ak=AKIDOUPloTlI4ESskPbVGX6D3nSQGvIKfK8aoA4y_f2xHfUf6S7HnNhKnm1MqyJ8PNKb&q-sign-time=1668006722;1668007622&q-key-time=1668006722;1668007622&q-header-list=&q-url-param-list=&q-signature=84c030c32f8e57cf1aa71cc0ef2dd8dfa0de2214\",\"cos_file_id\":\"HIGP8SNqpwtRc59lFJpR4kIbbbDvq0cRh59fSbwzHZ1ahKX7jpo0iDoqLawTP/LVlCazlzvjhkw6d7UyBgJjkzN3MqtI0xf3Xqq/6YGHfL/wldQ1bL64FWhNP4anWCvcnGK5mSLDV4yJEHweoO6AyW6DNRlJBKqiuMrQlurT0UnOhQQ2FaYmkA==\",\"file_id\":\"cloud://prod-9ge6u8sn7684a421.7072-prod-9ge6u8sn7684a421-1314693576/web/smb-data.pdf\",\"errmsg\":\"ok\",\"url\":\"https://cos.ap-shanghai.myqcloud.com/7072-prod-9ge6u8sn7684a421-1314693576/web/smb-data.pdf\",\"token\":\"h5kidHboTJc6Ylxx0QQgAi5JVZyOEsXa8ac1fff2902d4c5e04f803efb49cbc6dQcvvd9DvAavx7nzhcvRr7XvGEG_eH1FwYW_4DxlHOeA_L6YYgCFX6uZnkqPvnvUnTx_IMrzQg6Gt3GXNyixN-V1sTrnjX9UJCV7QANH7Pj8WTqHXmEXAx6ldmaFLGkjoBuf7skiJZy6VbUvL9a6FXmsnLxtUreq4xSlYhOxi2KQ7GRhVgxNSC2KzrhcCoURiSwmhrWAAL5hmtdVOVKnvItvvM3tJ62vQzA5VvEafSqoJzZoyRK8upSo8uoiI___-FupBqyK9Tc-LJxOIpNnnYgkiKTyQPMTDysykk7nrBnDYNlhHa6viPEwPuV9MLRBlgZXlKhdCr126lNKPWYVv5_pVFGa9lU9iRDagaA2pYPVscc_qvsBcHUkbNXdQf76O\"}";
        JSONObject respJson = JSON.parseObject(msg);
        respJson.put("key", "/web/smb-data.pdf");
        adminFileInfoService.upload(respJson);
    }

}
