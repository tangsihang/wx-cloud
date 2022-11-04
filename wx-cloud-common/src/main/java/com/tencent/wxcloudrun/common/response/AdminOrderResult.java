package com.tencent.wxcloudrun.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
public class AdminOrderResult implements Serializable {

    private String openid;
    private String outTradeNo;
    private Integer amount;
    private String currency;
    private String businessType;
    private String orderType;
    private String mobile;
    private Date created;
}
