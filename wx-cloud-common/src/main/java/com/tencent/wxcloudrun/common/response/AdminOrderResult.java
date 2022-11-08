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
    private String status;
    private String businessType;
    private String orderType;
    private Integer adsId;
    private String mobile;
    private String img;
    private String video;
    private String title;
    private Integer fee;
    private String category;
    private Date created;
}
