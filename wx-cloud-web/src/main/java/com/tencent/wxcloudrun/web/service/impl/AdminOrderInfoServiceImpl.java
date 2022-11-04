package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.entity.UserEntity;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.dao.repository.UserRepository;
import com.tencent.wxcloudrun.web.service.AdminOrderInfoService;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangsh
 * @date 2022/11/04
 */

@Service
@Slf4j
public class AdminOrderInfoServiceImpl implements AdminOrderInfoService {


    @Autowired
    private AdsOrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PageDTO<AdsOrderEntity> page(BasePageParam param) {
        IPage<AdsOrderEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<AdsOrderEntity> record = orderRepository.page(page);
        return PageUtils.copy(record);
    }

    @Override
    public JSONObject detail(BaseOrderNoParam param) {
        AdsOrderEntity order = orderRepository.getOneByOrderNo(param.getOutTradeNo());
        JSONObject result = null;
        if (order != null) {
            result = JSONObject.parseObject(order.getResp());
        }
        return result;
    }

    @Override
    public UserInfoResult linkUserInfo(BaseWxUserParam param) {
        UserEntity userEntity = userRepository.getOneByOpenId(param.getOpenid());
        if (userEntity == null) {
            throw new BizException(ErrorCode.BIZ_BREAK, "用户信息不存在!");
        }
        UserInfoResult result = new UserInfoResult();
        BeanUtils.copyProperties(userEntity, result);
        return result;
    }
}
