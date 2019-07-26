package com.cmgun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmgun.entity.BusiRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 业务请求流水记录Mapper
 *
 * @author chenqilin
 * @Date 2019/7/22
 */
@Mapper
public interface BusiRequestMapper extends BaseMapper<BusiRequest> {

}