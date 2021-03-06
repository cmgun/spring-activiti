package com.cmgun.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenqilin
 * @date 2019/8/1
 */
@Data
@Builder
public class AuditDTO implements Serializable {

    private static final long serialVersionUID = 1282054737914268616L;

    private String auditor;

    private String auditorName;

    private Object auditResult;

    private String comment;

    private Date auditTime;
}
