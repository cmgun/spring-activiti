package com.cmgun.api.model;

import com.cmgun.api.common.Request;
import com.cmgun.api.common.TaskContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程启动请求
 *
 * @author chenqilin
 * @date 2019/7/17
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("流程开启参数")
public class ProcessStartRequest extends Request {

    private static final long serialVersionUID = -1992424046597943733L;

    @NotNull(message = "流程定义key不能为空")
    @ApiModelProperty("流程定义key，process标签的id")
    private String processDefinitionKey;

    @NotNull(message = "流程的业务id不能为空")
    @ApiModelProperty("流程的业务id，业务系统保证唯一性")
    private String businessKey;

    @NotBlank(message = "流程类型不能为空")
    @ApiModelProperty("流程类型，process标签的namespace")
    private String category;

    @NotNull(message = "流程上下文不能为空")
    @ApiModelProperty("流程上下文，包含必要的业务数据")
    private TaskContext taskContext;
}
