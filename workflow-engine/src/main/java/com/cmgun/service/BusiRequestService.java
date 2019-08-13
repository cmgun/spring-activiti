package com.cmgun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmgun.api.common.Request;
import com.cmgun.entity.BusiRequest;

/**
 * 业务请求记录
 *
 * @author chenqilin
 * @date 2019/7/22
 */
public interface BusiRequestService extends IService<BusiRequest> {

    /**
     * 成功处理更新请求记录，更新失败则抛业务异常
     *
     * @param request 请求
     */
    void updateSuccessReq(Request request);
}
