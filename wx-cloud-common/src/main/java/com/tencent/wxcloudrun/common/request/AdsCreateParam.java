package com.tencent.wxcloudrun.common.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tencent.wxcloudrun.common.expection.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author tangsh
 * @date 2022/10/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdsCreateParam {

    private String title;
    private String des;
    private String img;
    private String video;
    private Integer fee;
    private String content;
    private String category;
    private Integer virtualNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public void checkParam() {
        Assert.warnNotEmpty(getTitle(), "广告标题不能为空");
        Assert.warnNotEmpty(getImg(), "图片不能为空");
        Assert.warnNotEmpty(getFee(), "广告金额不能为空");
        Assert.warnNotEmpty(getDes(), "描述不能为空");
        Assert.warnNotEmpty(getContent(), "内容不能为空");
        Assert.warnNotEmpty(getCategory(), "类目不能为空");
    }
}
