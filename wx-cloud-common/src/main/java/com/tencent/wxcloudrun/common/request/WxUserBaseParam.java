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
public class WxUserBaseParam implements Serializable {

    @NotNull(message = "openid不能为空")
    @ApiModelProperty(value = "openid", required = true)
    private String openid;
}
