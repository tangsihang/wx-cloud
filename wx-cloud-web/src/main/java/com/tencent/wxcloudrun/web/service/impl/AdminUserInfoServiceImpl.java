package com.tencent.wxcloudrun.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.common.dto.PageDTO;
import com.tencent.wxcloudrun.common.expection.BizException;
import com.tencent.wxcloudrun.common.expection.ErrorCode;
import com.tencent.wxcloudrun.common.request.AdminUserLoginParam;
import com.tencent.wxcloudrun.common.request.BaseInviteCodeParam;
import com.tencent.wxcloudrun.common.request.BasePageParam;
import com.tencent.wxcloudrun.common.response.InvitePayDetailResult;
import com.tencent.wxcloudrun.common.response.InviteUserDetailResult;
import com.tencent.wxcloudrun.common.response.UserInfoResult;
import com.tencent.wxcloudrun.dao.entity.AdsOrderEntity;
import com.tencent.wxcloudrun.dao.entity.UserEntity;
import com.tencent.wxcloudrun.dao.entity.UserInviteCodeEntity;
import com.tencent.wxcloudrun.dao.entity.UserInviteRelateEntity;
import com.tencent.wxcloudrun.dao.repository.AdsOrderRepository;
import com.tencent.wxcloudrun.dao.repository.UserInviteCodeRepository;
import com.tencent.wxcloudrun.dao.repository.UserInviteRelateRepository;
import com.tencent.wxcloudrun.dao.repository.UserRepository;
import com.tencent.wxcloudrun.web.service.AdminUserInfoService;
import com.tencent.wxcloudrun.web.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    public PageDTO<UserInfoResult> page(BasePageParam param) {
        IPage<UserEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<UserEntity> record = userRepository.page(page);
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
            result.setInviteUserMobile(userEntity.getMobile());
            resultList.add(result);
        });
        return resultList;
    }

    @Override
    public List<InvitePayDetailResult> invitePayDetail(BaseInviteCodeParam param) {
        return null;
    }




    private List<UserInfoResult> transferRecordForPage(IPage<UserEntity> record) {
        List<UserInfoResult> userInfoResultList = Lists.newArrayList();
        record.getRecords().forEach(it -> {
            UserInfoResult result = buildUserInfoResult(it);
            userInfoResultList.add(result);
        });
        return userInfoResultList;
    }


    private UserInfoResult buildUserInfoResult(UserEntity userEntity) {
        String openid = userEntity.getOpenid();
        UserInfoResult result = new UserInfoResult();
        BeanUtils.copyProperties(userEntity, result);
        UserInviteCodeEntity inviteCodeEntity = codeRepository.getOneByOpenId(openid);
        //若用户存在邀请码
        if (inviteCodeEntity != null) {
            result.setInviteCode(inviteCodeEntity.getInviteCode());
            List<UserEntity> inviteUserList = userRepository.queryByInviteCode(inviteCodeEntity.getInviteCode());
            if (!CollectionUtils.isEmpty(inviteUserList)) {
                List<String> inviteUserOpenIdList = inviteUserList.stream().map(UserEntity::getOpenid).collect(Collectors.toList());
                List<AdsOrderEntity> payOrderList = adsOrderRepository.queryByOpenIdList(inviteUserOpenIdList);
                result.setInviteNum(inviteUserList.size());
                result.setOrderPayNum(payOrderList.size());
            } else {
                result.setInviteNum(0);
                result.setOrderPayNum(0);
            }
        }
        return result;
    }
}
