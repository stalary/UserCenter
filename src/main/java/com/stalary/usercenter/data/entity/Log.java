package com.stalary.usercenter.data.entity;

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
     * 日志内容
     */
    private String content;

    /**
     * 关联的用户id
     */
    private Long userId;
}