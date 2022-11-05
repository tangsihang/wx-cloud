package com.tencent.wxcloudrun.common.request;

import com.tencent.wxcloudrun.common.expection.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer fee;
    private String content;
    private String category;
    private Integer virtualNum;

    public void checkParam() {
        Assert.warnNotEmpty(getTitle(), "广告标题不能为空");
        Assert.warnNotEmpty(getImg(), "图片不能为空");
        Assert.warnNotEmpty(getFee(), "广告金额不能为空");
        Assert.warnNotEmpty(getDes(), "描述不能为空");
        Assert.warnNotEmpty(getContent(), "内容不能为空");
        Assert.warnNotEmpty(getCategory(), "类目不能为空");
    }
}
