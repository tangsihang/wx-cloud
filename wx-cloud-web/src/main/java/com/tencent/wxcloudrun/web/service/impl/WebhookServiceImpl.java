package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.common.constants.WxEventEnum;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.entity.AdsOrderLogEntity;
import com.tencent.wxcloudrun.dao.repository.AdsOrderLogRepository;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.web.service.WebhookService;
import com.tencent.wxcloudrun.web.utils.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangsh
 * @date 2022/10/31
 */
@Service
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    @Autowired
    private AdsOrderRepository adsOrderRepository;
    @Autowired
    private AdsOrderLogRepository adsOrderLogRepository;

    @Override
    public JSONObject respWxPayHook(JSONObject req) {
        log.info("微信下单-回调成功:{}", req.toJSONString());
        String outTradeNo = req.getString("outTradeNo");
        AdsOrderEntity orderEntity = adsOrderRepository.getOneByOrderNo(outTradeNo);
        if (orderEntity == null) {
            log.warn("原始订单号不存在!:{}", outTradeNo);
            return WxUtils.respFail();
        }
        updatePayOrder(req, outTradeNo);
        WxEventEnum event = WxEventEnum.CREATE_ORDER_WEBHOOK;
        saveOrderLog(req, req, event);
        return WxUtils.respSuccess();
    }

    @Override
    public JSONObject respWxRefundHook(JSONObject req) {
        log.info("微信退款-回调成功:{}", req.toJSONString());
        String outTradeNo = req.getString("outTradeNo");
        AdsOrderEntity orderEntity = adsOrderRepository.getOneByOrderNo(outTradeNo);
        if (orderEntity == null) {
            log.error("支付订单不存在,{}", outTradeNo);
            throw new BizException(ErrorCode.BIZ_BREAK, "支付订单不存在!");
        }
        outTradeNo = req.getString("outRefundNo");
        Integer totalFee = req.getInteger("totalFee");
        //新增退款单表
        buildRefundOrder(req, orderEntity.getOpenid(), outTradeNo, totalFee);
        return WxUtils.respSuccess();
    }

    @Override
    public JSONObject respWxUploadHook(JSONObject req) {
        log.info("微信上传-回调成功:{}", req.toJSONString());

        return null;
    }

    private void saveOrderLog(JSONObject reqJson, JSONObject respJson, WxEventEnum event) {
        AdsOrderLogEntity entity = new AdsOrderLogEntity();
        String openid = reqJson.getString("subOpenid");
        String outTradeNo = reqJson.getString("out_trade_no");
        entity.setOpenid(openid);
        entity.setEvent(event.name());
        entity.setReq(reqJson.toJSONString());
        entity.setResp(respJson.toJSONString());
        entity.setOutTradeNo(outTradeNo);
        adsOrderLogRepository.save(entity);
    }

    private void updatePayOrder(JSONObject req, String outTradeNo) {
        AdsOrderEntity update = new AdsOrderEntity();
        update.setResp(req.toJSONString());
        update.setStatus("SUCCESS");
        LambdaQueryWrapper<AdsOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdsOrderEntity::getOutTradeNo, outTradeNo);
        boolean flag = adsOrderRepository.update(update, queryWrapper);
        log.info("{}", flag);
    }

    private void buildRefundOrder(JSONObject req, String openid, String outTradeNo, Integer totalFee) {
        AdsOrderEntity order = new AdsOrderEntity();
        order.setOpenid(openid);
        order.setOutTradeNo(outTradeNo);
        order.setAmount(totalFee);
        order.setOrderType("REFUND");
        order.setBusinessType("ADS");
        order.setResp(req.toJSONString());
        adsOrderRepository.save(order);
    }
}
