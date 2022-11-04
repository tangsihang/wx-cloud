package com.tencent.wxcloudrun.dao.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.dao.entity.UserInviteRelateEntity;
import com.tencent.wxcloudrun.dao.mapper.UserInviteRelateMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tangsh
 * @date 2022/11/01
 */
@Repository
public class UserInviteRelateRepository extends BaseRepository<UserInviteRelateMapper, UserInviteRelateEntity> {

    public List<UserInviteRelateEntity> queryByInviteCode(String inviteCode) {
        LambdaQueryWrapper<UserInviteRelateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInviteRelateEntity::getInviteCode, inviteCode);
        return this.list(wrapper);
    }
}
