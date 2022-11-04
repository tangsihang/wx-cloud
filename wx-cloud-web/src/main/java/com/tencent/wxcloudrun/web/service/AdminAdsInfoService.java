package com.tencent.wxcloudrun.web.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;

/**
 * @author tangsh
 * @date 2022/11/02
 */
public interface AdminAdsInfoService {

    PageDTO<AdsInfoEntity> page(AdsPageParam param);

    AdsInfoEntity detail(AdsBaseParam param);

    void create(AdsCreateParam param);

    void edit(AdsEditParam param);

    void on(AdsBaseParam param);

    void off(AdsBaseParam param);

    JSONObject upload( UploadParam param);

}
