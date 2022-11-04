package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;

/**
 * @author tangsh
 * @date 2022/10/31
 */
public interface AdminOrderInfoService {

    PageDTO<AdsOrderEntity> page(BasePageParam param);

    JSONObject detail(BaseOrderNoParam param);

    UserInfoResult linkUserInfo(BaseWxUserParam param);
}
