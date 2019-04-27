package com.zgw.jdbc.orm.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Table(name = "user_tbl")
@Entity
public class User implements java.io.Serializable {

    /**
     * 插入的主键ID
     */
    @Id
    private Integer id;
    private String name;

    private Integer age;

    private String addr;
}