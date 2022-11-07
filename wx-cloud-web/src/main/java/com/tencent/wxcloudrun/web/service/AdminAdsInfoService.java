package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.common.response.AdsPageInfoResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/02
 */
public interface AdminAdsInfoService {

    PageDTO<AdsPageInfoResult> page(AdsPageParam param);

    AdsInfoEntity detail(BaseAdsParam param);

    void create(AdsCreateParam param);

    void edit(AdsEditParam param);

    void on(BaseAdsParam param);

    void off(BaseAdsParam param);

    JSONObject upload(UploadParam param);

    List<InviteUserDetailResult> payUserDetail(BaseAdsParam param);

    void updateVirtualNum(AdsVirtualNumEditParam param);

    HSSFWorkbook export(AdsPageParam param);

}
