package com.cmgun.utils;


import com.cmgun.api.common.PageQuery;

/**
 * 分页查询工具类
 *
 * @author chenqilin
 * @Date 2019/7/31
 */
public class PageUtil {

    /**
     * 根据总数和分页查询条件判断，分页查询是否已经超出可查范围
     *
     * @param total 总数
     * @param pageRequest 分页查询条件
     * @return true/false
     */
    public static boolean isOutOfRange(long total, PageQuery pageRequest) {
        return total < (pageRequest.getPageNo() - 1) * pageRequest.getPageSize();
    }
}
