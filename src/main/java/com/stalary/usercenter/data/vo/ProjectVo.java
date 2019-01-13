/**
 * @(#)ProjectVo.java, 2019-01-13.
 *
 * Copyright 2019 Stalary.
 */
package com.stalary.usercenter.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @model ProjectVo
 * @description 项目信息Vo
 * @author lirongqian
 * @since 2019/01/13
 * @field name 项目名称
 * @field createTime 开始使用时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVo {

    private String name;

    private Date createTime;
}