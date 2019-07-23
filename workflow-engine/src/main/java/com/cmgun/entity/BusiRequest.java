package com.cmgun.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.cmgun.api.common.Request;
import com.cmgun.entity.enums.BusiTypeEnum;
import com.cmgun.entity.enums.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 业务请求流水记录
 *
 * @author chenqilin
 * @Date 2019/7/22
 */
@Data
@TableName("busi_request")
@Builder
@AllArgsConstructor
public class BusiRequest {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务类型
     */
    @TableField
    private Integer busiType;

    /**
     * 请求来源
     */
    @TableField
    private String source;

    /**
     * 请求流水号
     */
    @TableField
    private String clientReqNo;

    /**
     * 请求状态
     */
    @TableField
    private Integer status;

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

    /**
     * 备注
     */
    @TableField
    private String remark;

    public BusiRequest() {
    }

    public BusiRequest(BusiTypeEnum busiTypeEnum, Request request, RequestStatusEnum statusEnum) {
        this.busiType = busiTypeEnum.getValue();
        this.source = request.getSource();
        this.clientReqNo = request.getRequestNo();
        this.status = statusEnum.getValue();
        this.createTime = new Date();
    }
}
