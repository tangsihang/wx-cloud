package com.tencent.wxcloudrun.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
public class InviteUserDetailResult implements Serializable {

    private String openid;
    private String nickname;
    private String inviteCode;
    private String inviteOpenid;
    private String inviteUserMobile;
    private Date created;
}
