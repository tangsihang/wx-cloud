package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.AdminOrderPageParam;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.AdminOrderResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.entity.UserEntity;
import com.tencent.wxcloudrun.dao.repository.AdsInfoRepository;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.dao.repository.UserRepository;
import com.tencent.wxcloudrun.web.service.AdminOrderInfoService;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private AdsInfoRepository adsInfoRepository;

    @Override
    public PageDTO<AdminOrderResult> page(AdminOrderPageParam param) {
        IPage<AdsOrderEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<AdsOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(param.getStatus())) {
            queryWrapper.eq(AdsOrderEntity::getStatus, param.getStatus());
        }
        if (StringUtils.hasLength(param.getOutTradeNo())) {
            queryWrapper.like(AdsOrderEntity::getOutTradeNo, param.getOutTradeNo());
        }
        IPage<AdsOrderEntity> record = orderRepository.page(page);
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

    private List<AdminOrderResult> transferRecordForPage(IPage<AdsOrderEntity> record) {
        return record.getRecords().stream().map(this::buildResult).collect(Collectors.toList());
    }

    private AdminOrderResult buildResult(AdsOrderEntity orderEntity) {
        AdminOrderResult result = new AdminOrderResult();
        BeanUtils.copyProperties(orderEntity, result);
        String openid = orderEntity.getOpenid();
        UserEntity entity = userRepository.getOneByOpenId(openid);
        if (entity != null) {
            result.setMobile(entity.getMobile());
        }
        AdsInfoEntity adsInfoEntity = adsInfoRepository.getById(orderEntity.getMid());
        if (adsInfoEntity != null) {
            result.setCategory(adsInfoEntity.getCategory());
            result.setTitle(adsInfoEntity.getTitle());
            result.setFee(adsInfoEntity.getFee());
        }
        return result;
    }


}
