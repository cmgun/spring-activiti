package com.cmgun.controller;

import com.cmgun.api.common.Response;
import com.cmgun.api.model.ProgressDeployRequest;
import com.cmgun.api.service.ActProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程管理Service
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Slf4j
@RestController
@RequestMapping("actProgress")
public class ActProgressController implements ActProgressService {

    @Override
    public Response deploy(ProgressDeployRequest request) {
        log.info("in deploy");
        return null;
    }

    @Override
    public Response start() {
        return null;
    }

    @Override
    public Response suspend() {
        return null;
    }

    @Override
    public Response activate() {
        return null;
    }
}
