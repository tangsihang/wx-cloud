package com.tencent.wxcloudrun.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author tangsh
 * @date 2022/11/01
 */
@Data
@TableName("user_invite_relate")
public class UserInviteRelateEntity extends BaseDO {

    private String openid;
    private String inviteCode;
    private String inviteOpenid;

}
