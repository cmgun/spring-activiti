package com.cmgun.api.service;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.common.Response;
import com.cmgun.api.model.History;
import com.cmgun.api.model.HistoryQueryRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 历史记录管理
 *
 * @author chenqilin
 * @Date 2019/7/29
 */
@FeignClient(value = "workflow-engine", path = "history")
public interface ActHistoryService {

    @ApiOperation("历史任务查询")
    @PostMapping("query")
    Response<PageResult<History>> query(@RequestBody @Validated @ApiParam(value = "查询参数", required = true)
                                  HistoryQueryRequest request);
}
