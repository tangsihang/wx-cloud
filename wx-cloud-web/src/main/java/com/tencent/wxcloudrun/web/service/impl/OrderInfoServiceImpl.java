package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.repository.AdsInfoRepository;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.web.service.OrderInfoService;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangsh
 * @date 2022/11/07
 */

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private AdsOrderRepository orderRepository;
    @Autowired
    private AdsInfoRepository adsInfoRepository;

    @Override
    public PageDTO<AdminOrderResult> page(String openid, BasePageParam param) {
        IPage<AdsOrderEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<AdsOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdsOrderEntity::getOpenid, openid);
        IPage<AdsOrderEntity> record = orderRepository.page(page, queryWrapper);
        List<AdminOrderResult> resultList = transferRecordForPage(record);
        return PageUtils.copyWithRecords(record, resultList);
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

    private List<AdminOrderResult> transferRecordForPage(IPage<AdsOrderEntity> record) {
        return record.getRecords().stream().map(this::buildResult).collect(Collectors.toList());
    }

    private AdminOrderResult buildResult(AdsOrderEntity orderEntity) {
        AdminOrderResult result = new AdminOrderResult();
        BeanUtils.copyProperties(orderEntity, result);
        AdsInfoEntity adsInfoEntity = adsInfoRepository.getById(orderEntity.getMid());
        if (adsInfoEntity != null) {
            result.setAdsId(adsInfoEntity.getId());
            result.setCategory(adsInfoEntity.getCategory());
            result.setTitle(adsInfoEntity.getTitle());
            result.setFee(adsInfoEntity.getFee());
            result.setImg(adsInfoEntity.getImg());
            result.setVideo(adsInfoEntity.getVideo());
        }
        return result;
    }
}
