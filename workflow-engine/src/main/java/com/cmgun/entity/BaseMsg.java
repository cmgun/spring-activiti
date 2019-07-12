package com.cmgun.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "base_msg")
public class BaseMsg implements Serializable {
    private static final long serialVersionUID = -4011711780205734345L;

    @Id
    private int id;

    @Column(name = "msg")
    private String msg;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_id")
    private String createId;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_id")
    private String updateId;
}
