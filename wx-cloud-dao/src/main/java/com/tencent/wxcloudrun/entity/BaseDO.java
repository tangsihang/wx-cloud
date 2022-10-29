
package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseDO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Date created;
    private Date modified;
    @TableLogic
    private Integer flag;
}