package com.tencent.wxcloudrun;

import com.alibaba.fastjson.JSON;
import com.tencent.wxcloudrun.client.WxClient;
import com.tencent.wxcloudrun.model.request.AdsPageParam;
import com.tencent.wxcloudrun.model.dto.PageDTO;
import com.tencent.wxcloudrun.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.model.request.WxPrePayParam;
import com.tencent.wxcloudrun.service.AdsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WxCloudRunApplication.class})
public class WxTest {

    @Value("${wx.created.order.url:http://api.weixin.qq.com/_/pay/unifiedorder}")
    public String WX_CREATE_ORDER_URL;
    @Value("${wx.v3.api.secret:xQauK8PrfXR0l9m4lV1J2vbXFj6ZOGfh}")
    public String WX_V3_API_SECRET;

    @Autowired
    private WxClient wxClient;

    @Autowired
    private AdsInfoService adsInfoService;

    @Test
    public void test_query_ads_page() {
        int pageNo = 0;
        int pageSize = 10;
        AdsPageParam param = AdsPageParam.builder().pageNo(pageNo).pageSize(pageSize).build();
        PageDTO<AdsInfoEntity> pageDTO = adsInfoService.page(param);
        log.info("{}", JSON.toJSONString(pageDTO));
    }

    @Test
    public void test_wx_pay() {
        String msg = "{\"body\":\"测试wechat-pay\",\"callback_type\":2,\"container\":{\"path\":\"/webhook/v1/pay\",\"service\":\"pay\"},\"env_id\":\"prod-9ge6u8sn7684a421\",\"openid\":\"oXPeb4gGUWuARbGkIcvrb3PXTb30\",\"out_trade_no\":\"2021WERUN16478406876373\",\"spbill_create_ip\":\"218.74.6.72\",\"sub_mch_id\":\"1712734762\",\"total_fee\":1}";
        WxPrePayParam param = JSON.parseObject(msg, WxPrePayParam.class);
        wxClient.prePay(param);
//        String sign = PayUtils.createSign(WX_V3_API_SECRET, maps);
//        reqJson.put("sign", sign);
//        System.out.println(reqJson.toJSONString());
//        System.out.println(sign);

    }

}
