package com.tencent.wxcloudrun.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
public class InvitePayDetailResult implements Serializable {

    private String openid;
}