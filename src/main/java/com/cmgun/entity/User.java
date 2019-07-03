package com.cmgun.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chenqilin
 * @Date 2019/6/27
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    private long id;

    @Column(name = "name")
    private String name;

}
