package com.tencent.wxcloudrun.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderPageParam extends BasePageParam implements Serializable {

    private String outTradeNo;
    private String status;
}
