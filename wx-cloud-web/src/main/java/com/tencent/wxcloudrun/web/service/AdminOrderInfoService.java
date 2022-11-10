package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.AdminOrderPageParam;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author tangsh
 * @date 2022/10/31
 */
public interface AdminOrderInfoService {

    PageDTO<AdminOrderResult> page(AdminOrderPageParam param);

    JSONObject detail(BaseOrderNoParam param);

    UserInfoResult linkUserInfo(BaseWxUserParam param);

    String export(AdminOrderPageParam param);


}
