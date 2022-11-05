package com.tencent.wxcloudrun.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangsh
 * @date 2022/11/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResult implements Serializable {

    private String openid;
    private String nickname;
    private String avatarUrl;
    private Integer gender;
    private String mobile;
    private String inviteCode;
    //邀请人数
    private Integer inviteNum;
    //邀请付费人数
    private Integer inviteOrderPayNum;
    //已报名活动数据
    private Integer orderPayNum;
    private Date created;

}
