package com.tencent.wxcloudrun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdsPageParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.request.UserLoginParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.web.WxCloudRunApplication;
import com.tencent.wxcloudrun.web.service.AdsInfoService;
import com.tencent.wxcloudrun.web.service.OrderInfoService;
import com.tencent.wxcloudrun.web.service.UserInfoService;
import com.tencent.wxcloudrun.web.service.WebhookService;
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
    private AdsInfoService adsInfoService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WebhookService webhookService;


    @Test
    public void test_query_ads_page() {
        int pageNo = 0;
        int pageSize = 20;
        AdsPageParam param = new AdsPageParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        PageDTO<AdsInfoEntity> pageDTO = adsInfoService.page(param);
        log.info("{}", JSON.toJSONString(pageDTO));
    }

    @Test
    public void test_query_order_page() {
        int pageNo = 0;
        int pageSize = 2;
        String openid = "oXPeb4gGUWuARbGkIcvrb3PXTb30";
        BasePageParam param = new BasePageParam();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        PageDTO<AdminOrderResult> pageDTO = orderInfoService.page(openid, param);
        log.info("{}", JSON.toJSONString(pageDTO));
    }

    @Test
    public void get_user_info() {
        String msg = "{\"code\":\"041tVGFa1LRKaE0JjJIa1n5QyX1tVGFi\",\"iv\":\"uAxz60obe+xAHakCJkOvAw==\",\"encryptedData\":\"BKyCdVKPBQxYqiii1SCWR3NcFyAfkMYh6lyJ7RGPTk0l0rbWiLwsevBAHfqH7ey0WeEzB2+wb20ta1dlIlUv87Qz5RZmuxlc5mSGl9kKd42zI/PNt1gaArjIkpwQND6f1mLGS24fHszAn3B92tOZYk/OVN/3gjNUIv9Yl077UFpx0Z8TdAU/mNfoKMHBwxkSn4Fpf8GJHOQQ0X79Ej0bp77uUsYBtZtPj4wVZ5cPbAAzMfzDWXV8hzmhe7bFUicrRuaASmiDpokWF+/EEpYii2PsX5itUyKtJUtQ41gymjBbLwqCeXTAvvDI3DqH+h4rg9ZtpY6ss0WG5OVnLrOUh+QIhXSrPVahCQddel/2OUorc9OpX+GSb+JmfhVtFiKwGaJboeF/kyRT81w12iJSAg==\",\"signature\":\"a3946f98bff3138e25934440b0d6ee6cf75ec606\",\"rawData\":\"{\\\"nickName\\\":\\\"铭\\\",\\\"gender\\\":0,\\\"language\\\":\\\"zh_CN\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"country\\\":\\\"\\\",\\\"avatarUrl\\\":\\\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKeQj8NaUBPGkrS0ricGiccRC2JtARwU6vbbeOtQWI1naicNqxL7icHBD4x2iaEV9wicRcWwRBpdo7pxKFA/132\\\"}\"}";
        UserLoginParam param = JSONObject.parseObject(msg, UserLoginParam.class);
        String openid = "";
        userInfoService.login(openid, param);
    }


    @Test
    public void test_pay_webhook_() {
        String msg = "{\"cashFee\":1,\"timeEnd\":\"20221108205826\",\"mchId\":\"1800008281\",\"openid\":\"oPoo441ctvw8R0EwYeM8oT0bVNbo\",\"bankType\":\"OTHERS\",\"resultCode\":\"SUCCESS\",\"subMchId\":\"1633796573\",\"feeType\":\"CNY\",\"isSubscribe\":\"N\",\"nonceStr\":\"49ec14a692469297\",\"transactionId\":\"4200001612202211085435844559\",\"returnCode\":\"SUCCESS\",\"totalFee\":1,\"appid\":\"wxd2d16a504f24665e\",\"subOpenid\":\"oXPeb4gGUWuARbGkIcvrb3PXTb30\",\"outTradeNo\":\"PAY_670Lkxk11eec\",\"subIsSubscribe\":\"N\",\"tradeType\":\"JSAPI\",\"subAppid\":\"wx180962a99caf9ff5\"}";
        JSONObject req = JSONObject.parseObject(msg);
        webhookService.respWxPayHook(req);
    }

    @Test
    public void test_wx_login() {
        String msg = "{\n" +
                "\t\"code\": \"051vgFFa1JekdE0Bw2Ja1nYIbx3vgFF0\",\n" +
                "\t\"encryptedData\": \"fW8fOg9u9UUXg8qUW71aIRaGU/oSdL8XWeOhTLy9JQBut3qNyMvoasaz4JZ63ZMMqPy/i33GYMj9LIec9Qe2HNZl444mrFyM92muQ3rTvOx5U3Zf8JtqPx6LPLwF1pUk+1ewMC1SH2co3qHMjLBWq9UleW9po/pLotmrP6dpScBX2DMtVgX80lGIXAStFjj9yTebhdOQGWuH3LM/1oiJVpJ1GjGO5IkNSx1Fub6zGO7lFK5kRbjB4zZkAiIRA9ZHoLcBYiuxXcOqB2aRH7LzuSuyeQ+KAYOfvfdE+oXkk4n4CeAwtNWIG6pqjo0tPEMp996PqEswt1w/9QkbB6lAdVyBXXW5DZygFx7L6XYuLpPbkQVs0yRXj7fiho8JCGIfTILfd0tQkCqT3i02MqySuw==\",\n" +
                "\t\"inviteCode\": \"5Exhfd\",\n" +
                "\t\"iv\": \"69wuMWuwcyH2bw12sBTSwQ==\",\n" +
                "\t\"rawData\": \"{\\\"nickName\\\":\\\"辰北\\\",\\\"gender\\\":0,\\\"language\\\":\\\"zh_CN\\\",\\\"city\\\":\\\"\\\",\\\"province\\\":\\\"\\\",\\\"country\\\":\\\"\\\",\\\"avatarUrl\\\":\\\"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqW4PSlKibkNPn5TFCmFNISb6WAyWj0sLPJdMYv4h5icnBLwjWVHIW2V0f3KaGnoDuibiadtKEFeln57w/132\\\"}\",\n" +
                "\t\"signature\": \"6edee41d683a53ec2818815a85d117ec194cd48e\"\n" +
                "}";
        UserLoginParam req = JSONObject.parseObject(msg, UserLoginParam.class);
        String openid = "oXPeb4gGUWuARbGkIcvrb3PXTb30";
        userInfoService.login(openid, req);
    }


}

