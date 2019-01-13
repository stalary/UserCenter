/**
 * @(#)UserVo.java, 2019-01-13.
 *
 * Copyright 2019 Stalary.
 */
package com.stalary.usercenter.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UserVo
 *
 * @author lirongqian
 * @since 2019/01/13
 */
@Data
@AllArgsConstructor
public class UserVo {

    private Long userId;

    private String username;

    private Integer role;
}