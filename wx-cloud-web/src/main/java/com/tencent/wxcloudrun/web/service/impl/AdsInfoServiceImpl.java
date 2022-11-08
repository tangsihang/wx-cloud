package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.constants.AppConstant;
import com.tencent.wxcloudrun.common.constants.CategoryEnum;
import com.tencent.wxcloudrun.common.constants.WxEventEnum;
import com.tencent.wxcloudrun.common.dto.Container;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.AdsPageParam;
import com.tencent.wxcloudrun.common.request.BaseAdsParam;
import com.tencent.wxcloudrun.common.request.BaseOrderNoParam;
import com.tencent.wxcloudrun.common.request.WxPrePayParam;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.entity.AdsOrderLogEntity;
import com.tencent.wxcloudrun.dao.repository.AdsInfoRepository;
import com.tencent.wxcloudrun.dao.repository.AdsOrderLogRepository;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.web.client.WxPayClient;
import com.tencent.wxcloudrun.web.service.AdsInfoService;
import com.tencent.wxcloudrun.web.utils.NonceUtil;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author tangsh
 * @date 2022/10/27
 */

@Service
@Slf4j
public class AdsInfoServiceImpl implements AdsInfoService {

    @Autowired
    private WxPayClient wxClient;
    @Autowired
    private AdsInfoRepository adsInfoRepository;
    @Autowired
    private AdsOrderLogRepository adsOrderLogRepository;
    @Autowired
    private AdsOrderRepository adsOrderRepository;

    @Override
    public PageDTO<AdsInfoEntity> page(AdsPageParam param) {
        IPage<AdsInfoEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<AdsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdsInfoEntity::getStatus, "ON");
        queryWrapper.ge(AdsInfoEntity::getEndTime, new Date());
        if (StringUtils.hasLength(param.getCategory())) {
            CategoryEnum categoryEnum = CategoryEnum.getByCode(param.getCategory());
            queryWrapper.eq(AdsInfoEntity::getCategory, categoryEnum.getCode());
        }
        if (StringUtils.hasLength(param.getTitle())) {
            queryWrapper.like(AdsInfoEntity::getTitle, param.getTitle());
        }
        IPage<AdsInfoEntity> record = adsInfoRepository.page(page, queryWrapper);

        return PageUtils.copy(record);
    }

    @Override
    public AdsInfoEntity detail(BaseAdsParam param) {
        return adsInfoRepository.getById(param.getId());
    }

    @Override
    public JSONObject prePay(String openid, String ip, WxPrePayParam param) {
        JSONObject reqJson = (JSONObject) JSONObject.toJSON(param);
        reqJson.put("openid", openid);
        reqJson.put("sub_mch_id", AppConstant.WX_MERCHANT_ID);
        reqJson.put("env_id", AppConstant.WX_ENV_ID);
        String outTradeNo = "PAY_" + NonceUtil.createNonce(12);
        reqJson.put("out_trade_no", outTradeNo);
        reqJson.put("spbill_create_ip", ip);
        reqJson.put("callback_type", 2);
        Container container = new Container();
        container.setPath(AppConstant.WEB_HOOK_PAY_PATH);
        container.setService(AppConstant.WX_APP_SERVER);
        reqJson.put("container", container);
        WxEventEnum event = WxEventEnum.UNIFIED_ORDER;
        JSONObject respJson = wxClient.prePay(reqJson);
        //预付款下单状态
        buildPayOrder(reqJson, openid, outTradeNo, param);
        saveOrderLog(reqJson, respJson, event);
        return respJson;
    }

    private void buildPayOrder(JSONObject req, String openid, String outTradeNo, WxPrePayParam param) {
        AdsOrderEntity order = new AdsOrderEntity();
        order.setOpenid(openid);
        order.setOutTradeNo(outTradeNo);
        order.setMid(param.getAdsId());
        order.setAmount(param.getTotal_fee());
        order.setOrderType("PAY");
        order.setBusinessType("ADS");
        order.setResp(req.toJSONString());
        adsOrderRepository.save(order);
    }


    @Override
    public JSONObject refund(String openid, BaseOrderNoParam param) {
        String outTradeNo = param.getOutTradeNo();
        AdsOrderEntity order = adsOrderRepository.getOneByOrderNo(outTradeNo);
        if (order == null) {
            log.error("支付订单不存在,{}", outTradeNo);
            throw new BizException(ErrorCode.BIZ_BREAK, "支付订单不存在!");
        }

        JSONObject reqJson = (JSONObject) JSONObject.toJSON(param);
        reqJson.put("openid", openid);
        reqJson.put("out_trade_no", outTradeNo);
        String outRefundNo = "REFUND_" + NonceUtil.createNonce(12);
        reqJson.put("out_refund_no", outRefundNo);
        reqJson.put("env_id", AppConstant.WX_ENV_ID);
        reqJson.put("sub_mch_id", AppConstant.WX_MERCHANT_ID);
        reqJson.put("total_fee", order.getAmount());
        reqJson.put("refund_fee", order.getAmount());
        reqJson.put("refund_desc", "微信退款");
        reqJson.put("callback_type", 2);
        Container container = new Container();
        container.setPath(AppConstant.WEB_HOOK_REFUND_PATH);
        container.setService(AppConstant.WX_APP_SERVER);
        reqJson.put("container", container);
        WxEventEnum event = WxEventEnum.REFUND_ORDER;
        JSONObject respJson = wxClient.refund(reqJson);
        saveOrderLog(reqJson, respJson, event);
        return respJson;
    }

    @Override
    public JSONObject payQuery(String openid, BaseOrderNoParam param) {
        JSONObject reqJson = (JSONObject) JSONObject.toJSON(param);
        reqJson.put("sub_mch_id", AppConstant.WX_MERCHANT_ID);
        WxEventEnum event = WxEventEnum.QUERY_ORDER;
        JSONObject respJson = wxClient.payQuery(reqJson);
        saveOrderLog(reqJson, respJson, event);
        return respJson;
    }

    @Override
    public JSONObject payClose(String openid, BaseOrderNoParam param) {
        JSONObject reqJson = (JSONObject) JSONObject.toJSON(param);
        reqJson.put("sub_mch_id", AppConstant.WX_MERCHANT_ID);
        WxEventEnum event = WxEventEnum.CLOSE_ORDER;
        JSONObject respJson = wxClient.payClose(reqJson);
        saveOrderLog(reqJson, respJson, event);
        return respJson;
    }


    private void saveOrderLog(JSONObject reqJson, JSONObject respJson, WxEventEnum event) {
        AdsOrderLogEntity entity = new AdsOrderLogEntity();
        String openId = reqJson.getString("openid");
        String outTradeNo = reqJson.getString("out_trade_no");
        entity.setOpenid(openId);
        entity.setEvent(event.name());
        entity.setReq(reqJson.toJSONString());
        entity.setResp(respJson.toJSONString());
        entity.setOutTradeNo(outTradeNo);
        adsOrderLogRepository.save(entity);
    }
}
