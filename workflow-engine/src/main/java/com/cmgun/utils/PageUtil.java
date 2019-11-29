package com.cmgun.utils;


import com.cmgun.api.common.PageQuery;
import org.activiti.engine.query.Query;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

/**
 * 分页查询工具类
 *
 * @author chenqilin
 * @date 2019/7/31
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

    /**
     * 分页查询
     *
     * @param query 查询语句
     * @param request 分页查询参数
     * @param data 查询结果
     * @return 符合查询的总数
     */
    public static <U> long query(Query<? ,U> query, PageQuery request, List<U> data) {
        boolean needPageQuery = BooleanUtils.isNotTrue(request.getQueryAll());
        long count;
        if (needPageQuery) {
            count = query.count();
            if (PageUtil.isOutOfRange(count, request)) {
                return count;
            }
            // 分页查询
            data.addAll(query.listPage(request.getStartRow(), request.getPageSize()));
        } else {
            // 不分页查询
            data.addAll(query.list());
            count = data.size();
        }
        return count;
    }
}
