package com.tencent.wxcloudrun.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangsh
 * @date 2022/11/04
 */
@Data
public class UserPayDetailResult implements Serializable {

    private Integer adsId;
    private String title;
    private String category;
    private Integer fee;
    private Date created;
}
