package com.cmgun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmgun.api.common.Request;
import com.cmgun.entity.BusiRequest;
import com.cmgun.entity.enums.RequestStatusEnum;
import com.cmgun.mapper.BusiRequestMapper;
import com.cmgun.service.BusiRequestService;
import com.cmgun.utils.ExceptionUtil;
import org.springframework.stereotype.Service;

/**
 * 业务请求记录
 *
 * @author chenqilin
 * @Date 2019/7/22
 */
@Service
public class BusiRequestServiceImpl extends ServiceImpl<BusiRequestMapper, BusiRequest> implements BusiRequestService {

    @Override
    public void updateSuccessReq(Request request) {
        // 记录成功请求记录
        boolean updateResult = this.lambdaUpdate()
                .eq(BusiRequest::getClientReqNo, request.getRequestNo())
                .eq(BusiRequest::getSource, request.getSource())
                .update(BusiRequest.builder()
                        .status(RequestStatusEnum.SUCCESS.getValue())
                        .build());
        ExceptionUtil.businessException(!updateResult, "请求记录更新失败");
    }
}
