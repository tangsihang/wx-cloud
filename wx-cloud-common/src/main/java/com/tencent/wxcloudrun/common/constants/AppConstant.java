package com.tencent.wxcloudrun.common.constants;

import java.util.Date;

public class AppConstant {

    public static final String WX_APP_SERVER = "springboot-mvyj";

    public static final String WX_ENV_ID = "prod-9ge6u8sn7684a421";

    public static final String WX_MERCHANT_ID = "1633796573";

    public static final String WEB_HOOK_PAY_PATH = "/webhook/v1/pay";

    public static final String WEB_HOOK_REFUND_PATH = "/webhook/v1/refund";

    //------导出相关----------
    public static final String[] ADMIN_ORDER_EXPORT_TITLE = {"openid", "交易单号", "金额", "币种", "状态", "手机号", "广告标题", "广告类目"};

    public static final String[] ADMIN_USER_EXPORT_TITLE = {"openid", "昵称", "手机号", "邀请码", "邀请人数", "邀请下单人数", "已付费广告次数", "下单时间"};

    public static final String[] ADMIN_ADS_EXPORT_TITLE = {"广告id", "广告标题", "广告描述", "广告费用", "广告类目", "广告状态", "实际下单人数", "虚拟下单人数"};

    public static final String SHEET_NAME = "工作表1";

}
