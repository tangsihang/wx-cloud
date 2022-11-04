package com.tencent.wxcloudrun.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
public class InviteRelateResult implements Serializable {

    private String openid;
    private String inviteCode;
    private String inviteOpenid;
    private String inviteUserMobile;
}
