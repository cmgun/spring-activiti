package com.cmgun.consumer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 业务数据
 *
 * @author chenqilin
 * @Date 2019/7/25
 */
@Data
@TableName("base_msg")
public class BaseMsg {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private String msg;

    /**
     * 创建时间
     */
    @TableField
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE, update = "now()")
    private Date updateTime;

    @TableField
    private String createId;

    @TableField
    private String updateId;

    @TableField
    private Integer state;

}
