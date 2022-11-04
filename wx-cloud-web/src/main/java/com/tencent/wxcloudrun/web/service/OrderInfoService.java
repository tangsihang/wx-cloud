package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;

/**
 * @author tangsh
 * @date 2022/10/31
 */
public interface OrderInfoService {

    PageDTO<AdsOrderEntity> page(String openid, BasePageParam param);

    JSONObject detail(BaseOrderNoParam param);
}
