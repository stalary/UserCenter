/**
 * @(#)UserVo.java, 2019-01-13.
 *
 * Copyright 2019 Stalary.
 */
package com.stalary.usercenter.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @model UserVo
 * @description 用户信息Vo
 * @field id 用户id
 * @field username 用户名
 * @field role 用户角色
 * @field createTime 用户注册时间
 * @author lirongqian
 * @since 2019/01/13
 */
@Data
@AllArgsConstructor
public class UserVo {

    private Long id;

    private String username;

    private Integer role;

    private Date createTime;
}