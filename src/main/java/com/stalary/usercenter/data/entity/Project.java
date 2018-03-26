/**
 * @(#)Project.java, 2018-03-27.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Project
 *
 * @author lirongqian
 * @since 2018/03/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "project")
@Entity
public class Project extends BaseEntity {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 状态，-1为删除，0为正常，默认为0
     */
    @JsonIgnore
    private Integer status = 0;

}