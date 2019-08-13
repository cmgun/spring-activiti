package com.cmgun.entity.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenqilin
 * @date 2019/7/23
 */
@Data
@Builder
public class DeployDTO {

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程key
     */
    private String key;

    /**
     * 流程类别
     */
    private String category;

    /**
     * 流程文件
     */
    private MultipartFile file;
}
