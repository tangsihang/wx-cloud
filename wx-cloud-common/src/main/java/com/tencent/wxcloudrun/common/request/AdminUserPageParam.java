package com.tencent.wxcloudrun.common.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
public class AdminUserPageParam extends BasePageParam implements Serializable {

    private String nickname;

    private String mobile;
}
