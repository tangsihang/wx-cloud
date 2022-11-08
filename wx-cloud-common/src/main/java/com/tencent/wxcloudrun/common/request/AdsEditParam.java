package com.tencent.wxcloudrun.common.request;

import com.tencent.wxcloudrun.common.expection.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tangsh
 * @date 2022/10/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdsEditParam {

    private Integer id;
    private String title;
    private String des;
    private String img;
    private String video;
    private Integer fee;
    private String content;
    private String category;
    private Integer virtualNum;
    private Date startTime;
    private Date endTime;

    public void checkParam() {
        Assert.warnNotEmpty(getId(), "广告id不能为空");
    }
}
