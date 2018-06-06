/**
 * @(#)StatInfo.java, 2018-06-06.
 * <p>
 * Copyright 2018 Stalary.
 */
package com.stalary.usercenter.data.dto;

import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * StatInfo
 *
 * @author lirongqian
 * @since 2018/06/06
 */
@Data
@AllArgsConstructor
public class StatInfo {

    private Stat stat;

    private User user;
}