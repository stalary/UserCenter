package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @model Log
 * @description 日志对象
 * @field level 日志等级
 * @field content 日志内容
 * @field commonId 日志种类(user|project)
 * @field count 通用id
 * @field level 次数
 **/
@Data
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

    public Log(String level, String content, String type, Long commonId, Integer count) {
        this.level = level;
        this.content = content;
        this.type = type;
        this.commonId = commonId;
        this.count = count;
    }

    public Log() {
    }

    /**
     * 最后更新时间
     */
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}