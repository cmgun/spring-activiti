package com.cmgun.api.common;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * 分页请求响应体
 *
 * @author chenqilin
 * @Date 2019/7/29
 */
@Data
@Builder
@ApiModel("分页查询结果")
public class PageResult<T> {

    @ApiModelProperty(value = "总数量", required = true)
    private long total;

    @ApiModelProperty(value = "页码", required = true)
    private int pageNo;

    @ApiModelProperty(value = "分页大小", required = true)
    private int pageSize;

    @ApiModelProperty(value = "总页数", required = true)
    private int pageCount;

    @ApiModelProperty(value = "分页数据", required = true)
    private Collection<T> data;

    @ApiModelProperty(value = "是否忽略分页条件查询全部", notes = "如果该字段为true，返回全部符合条件的记录")
    private boolean queryAll;

    /**
     * 空结果
     *
     * @param pageQuery 分页查询请求
     * @param total 总数
     * @return 分页结果
     */
    public static PageResult empty(PageQuery pageQuery, long total) {
        return PageResult.builder()
                .pageNo(pageQuery.getPageNo())
                .pageSize(pageQuery.getPageSize())
                .total(total)
                .pageCount((int) Math.ceil((double) total / pageQuery.getPageSize()))
                .data(Lists.newArrayList())
                .build();
    }

    /**
     * 分页结果
     *
     * @param pageQuery 分页查询参数
     * @param total 总数
     * @param data 分页查询结果
     * @param <T> data内元素的类型
     * @return 分页结果
     */
    public static <T> PageResult<T> result(PageQuery pageQuery, long total, Collection<T> data) {
        return PageResult.<T>builder()
                .pageNo(pageQuery.getPageNo())
                .pageSize(pageQuery.getPageSize())
                .queryAll(pageQuery.getQueryAll())
                .total(total)
                .pageCount((int) Math.ceil((double) total / pageQuery.getPageSize()))
                .data(data)
                .build();
    }
}
