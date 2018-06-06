package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Log
 *
 * @author lirongqian
 * @since 2018/03/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "log")
@Entity
public class Log extends BaseEntity {

    /**
     * 日志等级
     */
    private String level;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 日志种类(user，project)
     */
    @JsonIgnore
    private String type;

    /**
     * 通用id
     */
    @JsonIgnore
    private Long commonId;

    /**
     * 次数
     */
    private Integer count = 1;
}