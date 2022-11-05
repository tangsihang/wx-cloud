package com.tencent.wxcloudrun.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author tangsh
 * @date 2022/10/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdsVirtualNumEditParam {

    @NotNull(message = "广告id不能为空")
    private Integer id;
    @NotNull(message = "编辑人数不能为空")
    private Integer virtualNum;
}
