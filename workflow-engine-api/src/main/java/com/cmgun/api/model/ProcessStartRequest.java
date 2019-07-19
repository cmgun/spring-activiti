package com.cmgun.api.model;

import com.cmgun.api.common.Request;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 流程启动请求
 *
 * @author chenqilin
 * @Date 2019/7/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessStartRequest extends Request {

    private static final long serialVersionUID = -1992424046597943733L;

    @NotNull(message = "流程定义key不能为空")
    @ApiModelProperty("流程定义key，process标签的id")
    private String processDefinitionKey;

    @NotNull(message = "流程的业务id不能为空")
    @ApiModelProperty("流程的业务id，命名规则：业务模块标志:业务id")
    private String businessKey;

    @ApiModelProperty("流程上下文，包含必要的业务数据")
    private Object payload;
}
