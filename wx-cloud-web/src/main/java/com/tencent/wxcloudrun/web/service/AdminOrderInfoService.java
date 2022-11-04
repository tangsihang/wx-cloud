package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.OrderDetailParam;
import com.tencent.wxcloudrun.common.request.OrderPageParam;
import com.tencent.wxcloudrun.common.request.WxUserBaseParam;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;

/**
 * @author tangsh
 * @date 2022/10/31
 */
public interface AdminOrderInfoService {

    PageDTO<AdsOrderEntity> page(OrderPageParam param);

    JSONObject detail(OrderDetailParam param);

    UserInfoResult linkUserInfo(WxUserBaseParam param);
}
