package com.tencent.wxcloudrun.dto;

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
public class AdsPageParam {

    @NotNull(message = "起始页不能为空")
    private Integer pageNo;

    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}