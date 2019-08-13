package com.cmgun.consumer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmgun.consumer.entity.BaseMsg;
import com.cmgun.consumer.mapper.BaseMsgMapper;
import org.springframework.stereotype.Service;

/**
 * @author chenqilin
 * @date 2019/7/25
 */
@Service
public class BaseMsgServiceImpl extends ServiceImpl<BaseMsgMapper, BaseMsg> implements BaseMsgService {
}
