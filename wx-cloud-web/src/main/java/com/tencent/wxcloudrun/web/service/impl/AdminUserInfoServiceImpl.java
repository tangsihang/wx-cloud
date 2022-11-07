package com.tencent.wxcloudrun.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.common.constants.AppConstant;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.AdminUserPageParam;
import com.tencent.wxcloudrun.common.request.BaseInviteCodeParam;
import com.tencent.wxcloudrun.common.request.BaseWxUserParam;
import com.tencent.wxcloudrun.common.response.InvitePayDetailResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.common.response.UserPayDetailResult;
import com.tencent.wxcloudrun.dao.entity.*;
import com.tencent.wxcloudrun.dao.repository.*;
import com.tencent.wxcloudrun.web.service.AdminUserInfoService;
import com.tencent.wxcloudrun.web.utils.ExcelUtils;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author tangsh
 * @date 2022/11/04
 */

@Service
@Slf4j
public class AdminUserInfoServiceImpl implements AdminUserInfoService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsOrderRepository adsOrderRepository;
    @Autowired
    private AdsInfoRepository adsInfoRepository;
    @Autowired
    private UserInviteCodeRepository codeRepository;
    @Autowired
    private UserInviteRelateRepository relateRepository;


    private static final Map<String, String> admin_user_map = Maps.newHashMap();

    static {
        admin_user_map.put("username", "admin");
        admin_user_map.put("password", "a9527");
    }

    @Override
    public void login(AdminUserLoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();
        if (username.equals(admin_user_map.get("username")) && password.equals(admin_user_map.get("password"))) {
            log.info("登录成功~");
        } else {
            log.error("登录失败~,账户密码不正确~ {},{}", username, password);
            throw new BizException(ErrorCode.BIZ_BREAK, "登录失败~,账户密码不正确!");
        }
    }

    @Override
    public PageDTO<UserInfoResult> page(AdminUserPageParam param) {
        IPage<UserEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(param.getMobile())) {
            queryWrapper.like(UserEntity::getMobile, param.getMobile());
        }
        if (StringUtils.hasLength(param.getNickname())) {
            queryWrapper.like(UserEntity::getNickname, param.getNickname());
        }
        IPage<UserEntity> record = userRepository.page(page, queryWrapper);
        if (record.getTotal() == 0) {
            return new PageDTO<>();
        }
        List<UserInfoResult> resultList = transferRecordForPage(record);
        return PageUtils.copyWithRecords(record, resultList);
    }

    @Override
    public List<InviteUserDetailResult> inviteUserDetail(BaseInviteCodeParam param) {
        String inviteCode = param.getInviteCode();
        List<UserInviteRelateEntity> inviteRelateList = relateRepository.queryByInviteCode(inviteCode);
        if (CollectionUtils.isEmpty(inviteRelateList)) {
            return Lists.newArrayList();
        }
        List<InviteUserDetailResult> resultList = Lists.newArrayList();
        inviteRelateList.forEach(it -> {
            InviteUserDetailResult result = new InviteUserDetailResult();
            result.setInviteOpenid(it.getInviteOpenid());
            result.setInviteCode(it.getInviteCode());
            result.setCreated(it.getCreated());
            String inviteOpenid = it.getInviteOpenid();
            UserEntity userEntity = userRepository.getOneByOpenId(inviteOpenid);
            if (userEntity == null) {
                return;
            }
            result.setNickname(userEntity.getNickname());
            result.setInviteUserMobile(userEntity.getMobile());
            resultList.add(result);
        });
        return resultList;
    }

    @Override
    public List<InvitePayDetailResult> invitePayDetail(BaseInviteCodeParam param) {
        String inviteCode = param.getInviteCode();
        List<UserInviteRelateEntity> inviteRelateList = relateRepository.queryByInviteCode(inviteCode);
        if (CollectionUtils.isEmpty(inviteRelateList)) {
            return Lists.newArrayList();
        }
        List<InvitePayDetailResult> resultList = Lists.newArrayList();
        //遍历邀请人列表
        inviteRelateList.forEach(it -> {

            String inviteOpenid = it.getInviteOpenid();
            //先查出被邀请的人.
            UserEntity userEntity = userRepository.getOneByOpenId(inviteOpenid);
            if (userEntity == null) {
                return;
            }
            List<AdsOrderEntity> adsOrderEntityList = adsOrderRepository.queryByOpenId(inviteOpenid);
            if (CollectionUtils.isEmpty(adsOrderEntityList)) {
                return;
            }
            adsOrderEntityList.forEach(order -> {
                InvitePayDetailResult result = new InvitePayDetailResult();
                //先查出被邀请的人.
                result.setOpenid(userEntity.getOpenid());
                result.setNickname(userEntity.getNickname());
                result.setMobile(userEntity.getMobile());
                result.setOutTradeNo(order.getOutTradeNo());
                AdsInfoEntity adsInfoEntity = adsInfoRepository.getById(order.getMid());
                if (adsInfoEntity == null) {
                    return;
                }
                result.setTitle(adsInfoEntity.getTitle());
                result.setCategory(adsInfoEntity.getCategory());
                result.setFee(adsInfoEntity.getFee());
                result.setCreated(order.getCreated());
                result.setAdsId(adsInfoEntity.getId());
                resultList.add(result);
            });
        });
        return resultList;
    }


    @Override
    public List<UserPayDetailResult> userPayDetail(BaseWxUserParam param) {
        String openid = param.getOpenid();
        List<AdsOrderEntity> orderList = adsOrderRepository.queryByOpenId(openid);
        List<UserPayDetailResult> resultList = Lists.newArrayList();
        orderList.forEach(it -> {
            UserPayDetailResult result = new UserPayDetailResult();
            AdsInfoEntity adsInfoEntity = adsInfoRepository.getById(it.getMid());
            if (adsInfoEntity == null) {
                return;
            }
            result.setAdsId(adsInfoEntity.getId());
            result.setCategory(adsInfoEntity.getCategory());
            result.setTitle(adsInfoEntity.getTitle());
            result.setFee(adsInfoEntity.getFee());
            result.setCreated(it.getCreated());
            resultList.add(result);
        });
        return resultList;
    }

    @Override
    public HSSFWorkbook export(AdminUserPageParam param) {
        PageDTO<UserInfoResult> pageResult = page(param);
        return parseExcel(pageResult.getRecords());
    }

    private HSSFWorkbook parseExcel(List<UserInfoResult> resultList) {
        String[][] content = new String[resultList.size()][];
        for (int i = 0; i < resultList.size(); i++) {
            content[i] = new String[AppConstant.ADMIN_USER_EXPORT_TITLE.length];
            UserInfoResult data = resultList.get(i);
            content[i][0] = data.getOpenid();
            content[i][1] = data.getNickname();
            content[i][2] = data.getMobile();
            content[i][3] = data.getInviteCode();
            content[i][4] = String.valueOf(data.getInviteNum());
            content[i][5] = String.valueOf(data.getInviteOrderPayNum());
            content[i][6] = String.valueOf(data.getOrderPayNum());
            content[i][7] = String.valueOf(data.getCreated());
        }
        return ExcelUtils.getHSSFWorkbook(AppConstant.ADMIN_USER_EXPORT_TITLE, content);
    }


    private List<UserInfoResult> transferRecordForPage(IPage<UserEntity> record) {
        return record.getRecords().stream().map(this::buildUserInfoResult).collect(Collectors.toList());
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        return t -> set.add(keyExtractor.apply(t));
    }

    private UserInfoResult buildUserInfoResult(UserEntity userEntity) {
        String openid = userEntity.getOpenid();
        UserInfoResult result = new UserInfoResult();

        result.setOpenid(userEntity.getOpenid());
        result.setAvatarUrl(userEntity.getAvatarUrl());
        result.setMobile(userEntity.getMobile());
        result.setNickname(userEntity.getNickname());
        List<AdsOrderEntity> adsOrderEntityList = adsOrderRepository.queryByOpenId(openid);
        result.setOrderPayNum(adsOrderEntityList.size());
        result.setCreated(userEntity.getCreated());
        UserInviteCodeEntity inviteCodeEntity = codeRepository.getOneByOpenId(openid);
        //若用户存在邀请码
        if (inviteCodeEntity != null) {
            result.setInviteCode(inviteCodeEntity.getInviteCode());
            List<UserEntity> inviteUserList = userRepository.queryByInviteCode(inviteCodeEntity.getInviteCode());
            if (!CollectionUtils.isEmpty(inviteUserList)) {
                //要求人数 去重
                inviteUserList = inviteUserList.stream().filter(distinctByKey(UserEntity::getOpenid)).collect(Collectors.toList());
                List<String> inviteUserOpenIdList = inviteUserList.stream().map(UserEntity::getOpenid).collect(Collectors.toList());
                List<AdsOrderEntity> payOrderList = adsOrderRepository.queryByOpenIdList(inviteUserOpenIdList);
                payOrderList = payOrderList.stream().filter(distinctByKey(AdsOrderEntity::getOpenid)).collect(Collectors.toList());
                result.setInviteNum(inviteUserList.size());
                result.setInviteOrderPayNum(payOrderList.size());
            } else {
                result.setInviteNum(0);
                result.setInviteOrderPayNum(0);
            }
        }
        return result;
    }
}
