package com.tencent.wxcloudrun.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.common.constants.AdsStatusEnum;
import com.tencent.wxcloudrun.common.constants.CategoryEnum;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.*;
import com.tencent.wxcloudrun.common.response.AdsPageInfoResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.utils.DataUtils;
import com.tencent.wxcloudrun.dao.entity.AdsInfoEntity;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.entity.UserEntity;
import com.tencent.wxcloudrun.dao.repository.AdsInfoRepository;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.dao.repository.UserRepository;
import com.tencent.wxcloudrun.web.client.WxFileClient;
import com.tencent.wxcloudrun.web.service.AdminAdsInfoService;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangsh
 * @date 2022/11/02
 */
@Service
@Slf4j
public class AdminAdsInfoServerImpl implements AdminAdsInfoService {

    @Value("${wx.env.id:prod-9ge6u8sn7684a421}")
    public String WX_ENV_ID;

    @Autowired
    private AdsInfoRepository adsInfoRepository;
    @Autowired
    private AdsOrderRepository adsOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WxFileClient wxFileClient;

    @Override
    public PageDTO<AdsPageInfoResult> page(AdsPageParam param) {
        IPage<AdsInfoEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<AdsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(param.getCategory())) {
            CategoryEnum categoryEnum = CategoryEnum.getByCode(param.getCategory());
            queryWrapper.eq(AdsInfoEntity::getCategory, categoryEnum.getCode());
        }
        if (StringUtils.hasLength(param.getTitle())) {
            queryWrapper.like(AdsInfoEntity::getTitle, param.getTitle());
        }
        IPage<AdsInfoEntity> record = adsInfoRepository.page(page, queryWrapper);

        List<AdsPageInfoResult> resultList = transferRecordForPage(record);
        return PageUtils.copyWithRecords(record, resultList);
    }

    private List<AdsPageInfoResult> transferRecordForPage(IPage<AdsInfoEntity> record) {
        return record.getRecords().stream().map(this::buildAdsInfoResult).collect(Collectors.toList());
    }

    private AdsPageInfoResult buildAdsInfoResult(AdsInfoEntity adsInfoEntity) {
        AdsPageInfoResult result = new AdsPageInfoResult();
        BeanUtils.copyProperties(adsInfoEntity, result);
        List<AdsOrderEntity> orderList = adsOrderRepository.queryByMid(adsInfoEntity.getId());
        orderList = orderList.stream().filter(DataUtils.distinctByKey(AdsOrderEntity::getOpenid)).collect(Collectors.toList());
        result.setPayNum(orderList.size());
        return result;
    }

    @Override
    public AdsInfoEntity detail(BaseAdsParam param) {
        return adsInfoRepository.getById(param.getId());
    }


    @Override
    public void create(AdsCreateParam param) {
        param.checkParam();
        AdsInfoEntity entity = new AdsInfoEntity();
        BeanUtils.copyProperties(param, entity);
        adsInfoRepository.save(entity);

    }

    @Override
    public void edit(AdsEditParam param) {
        param.checkParam();
        AdsInfoEntity entity = checkAdsExist(param.getId());
        if (AdsStatusEnum.ON.name().equals(entity.getStatus())) {
            throw new BizException(ErrorCode.BIZ_BREAK, "上架状态,暂不支持编辑!");
        }
        AdsInfoEntity update = new AdsInfoEntity();
        BeanUtils.copyProperties(param, update);
        adsInfoRepository.updateById(update);
    }

    @Override
    public void on(BaseAdsParam param) {
        AdsInfoEntity entity = checkAdsExist(param.getId());
        if (AdsStatusEnum.ON.name().equals(entity.getStatus())) {
            throw new BizException(ErrorCode.BIZ_BREAK, "已经是上架状态,无需再上架!");
        }
        AdsInfoEntity update = new AdsInfoEntity();
        update.setId(param.getId());
        update.setStatus(AdsStatusEnum.ON.name());
        adsInfoRepository.updateById(update);
    }


    @Override
    public void off(BaseAdsParam param) {
        AdsInfoEntity entity = checkAdsExist(param.getId());
        if (AdsStatusEnum.OFF.name().equals(entity.getStatus())) {
            throw new BizException(ErrorCode.BIZ_BREAK, "已经是下架状态,无需再下架!");
        }
        AdsInfoEntity update = new AdsInfoEntity();
        update.setId(param.getId());
        update.setStatus(AdsStatusEnum.OFF.name());
        adsInfoRepository.updateById(update);
    }

    @Override
    public JSONObject upload(UploadParam param) {
        JSONObject reqJson = new JSONObject();
        reqJson.put("env", WX_ENV_ID);
        reqJson.put("path", param.getPath());
        return wxFileClient.upload(reqJson);
    }

    @Override
    public List<InviteUserDetailResult> payUserDetail(BaseAdsParam param) {
        Integer adsId = param.getId();
        List<AdsOrderEntity> orderList = adsOrderRepository.queryByMid(adsId);
        if (CollectionUtils.isEmpty(orderList)) {
            return Lists.newArrayList();
        }
        List<InviteUserDetailResult> resultList = Lists.newArrayList();
        orderList.forEach(it -> {
            InviteUserDetailResult result = new InviteUserDetailResult();
            UserEntity userEntity = userRepository.getOneByOpenId(it.getOpenid());
            if (userEntity == null) {
                return;
            }
            result.setOpenid(it.getOpenid());
            result.setNickname(userEntity.getNickname());
            result.setInviteUserMobile(userEntity.getMobile());
            resultList.add(result);
        });
        return resultList;
    }

    private AdsInfoEntity checkAdsExist(Integer id) {
        AdsInfoEntity entity = adsInfoRepository.getById(id);
        if (entity == null) {
            log.error("广告不存在 {}", id);
            throw new BizException(ErrorCode.BIZ_BREAK, "广告不存在!");
        }
        return entity;
    }
}
