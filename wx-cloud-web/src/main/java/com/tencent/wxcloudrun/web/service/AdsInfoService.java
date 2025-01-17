package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;

/**
 * @author tangsh
 * @date 2022/10/27
 */

public interface AdsInfoService {

    PageDTO<AdsInfoEntity> page(AdsPageParam param);

    AdsInfoEntity detail(BaseAdsParam param);

    JSONObject prePay(String openid, String ip, WxPrePayParam param);

    JSONObject refund(String openid, BaseOrderNoParam param);

    JSONObject payQuery(String openid, BaseOrderNoParam param);

    JSONObject payClose(String openid, BaseOrderNoParam param);
}
