package com.cmgun.api.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 分页查询请求
 *
 * @author chenqilin
 * @Date 2019/7/29
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = -7561737477095221176L;

    @NotNull(message = "每页大小不能为空")
    @Positive(message = "每页大小必须大于0")
    @ApiModelProperty("每页大小")
    private int pageSize;

    @NotNull(message = "页码不能为空")
    @Positive(message = "页码必须大于0")
    @ApiModelProperty("页码，从1开始")
    private int pageNo;

    @ApiModelProperty(value = "是否忽略分页条件查询全部", notes = "如果该字段为true，返回全部符合条件的记录")
    private Boolean queryAll;

    /**
     * 获取分页开始的行数
     *
     * @return 分页开始的行数
     */
    public int getStartRow() {
        return (pageNo - 1) * pageSize;
    }
}
