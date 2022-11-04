package com.tencent.wxcloudrun.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InviteDetailParam implements Serializable {

    @NotNull(message = "邀请码不能为空")
    private String inviteCode;
}
