package com.zgw.jdbc.orm.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 〈〉
 * @author gw.Zeng
 * @create 2019/4/27
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "c_agent_tbl")
public class Agent {
    private  int id;
    @Column(name = "user_id")
    private  Integer userId;
    @Column(name="agent_name")
    private String agentName;

}
