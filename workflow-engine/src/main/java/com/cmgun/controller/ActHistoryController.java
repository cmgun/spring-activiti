package com.cmgun.controller;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.common.Response;
import com.cmgun.api.model.History;
import com.cmgun.api.model.HistoryQueryRequest;
import com.cmgun.api.service.ActHistoryService;
import com.cmgun.service.ProcHistoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 历史任务管理
 *
 * @author chenqilin
 * @Date 2019/7/29
 */
@Api(tags = "history")
@Slf4j
@RestController
@RequestMapping("history")
public class ActHistoryController implements ActHistoryService {

    @Autowired
    private ProcHistoryService procHistoryService;

    @Override
    public Response<PageResult<History>> query(HistoryQueryRequest request) {
        PageResult<History> pageResult = procHistoryService.queryHistory(request);
        return Response.success("查询成功", pageResult);
    }
}
