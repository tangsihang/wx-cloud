package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.model.dto.PageDTO;
import com.tencent.wxcloudrun.model.request.OrderDetailParam;
import com.tencent.wxcloudrun.model.request.OrderPageParam;
import com.tencent.wxcloudrun.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.service.OrderInfoService;
import com.tencent.wxcloudrun.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private AdsOrderRepository orderRepository;

    @Override
    public PageDTO<AdsOrderEntity> page(String openid, OrderPageParam param) {
        IPage<AdsOrderEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<AdsOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdsOrderEntity::getOpenid, openid);
        IPage<AdsOrderEntity> record = orderRepository.page(page);
        return PageUtils.copy(record);
    }

    @Override
    public JSONObject detail(OrderDetailParam param) {
        AdsOrderEntity order = orderRepository.getOneByOrderNo(param.getOutTradeNo());
        JSONObject result = null;
        if (order != null) {
            result = JSONObject.parseObject(order.getResp());
        }
        return result;
    }
}
