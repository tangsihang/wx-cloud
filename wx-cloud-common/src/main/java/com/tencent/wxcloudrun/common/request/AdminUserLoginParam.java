package com.tencent.wxcloudrun.common.request;

import io.swagger.annotations.ApiModelProperty;
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
public class AdminUserLoginParam implements Serializable {

    @NotNull(message = "登录账户不能为空")
    @ApiModelProperty(value = "登录账户", required = true)
    public String username;
    @NotNull(message = "登录密码不能为空")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;
}
