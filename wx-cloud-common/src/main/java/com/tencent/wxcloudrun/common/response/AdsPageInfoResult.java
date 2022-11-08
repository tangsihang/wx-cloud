package com.tencent.wxcloudrun.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangsh
 * @date 2022/11/05
 */
@Data
public class AdsPageInfoResult implements Serializable {

    private Integer id;
    private String title;
    private String des;
    private String img;
    private String video;
    private Integer fee;
    private String content;
    private String category;
    private String status;

    private Integer payNum;
    private Integer virtualNum;
    private Date startTime;
    private Date endTime;
}
