/**
 * @(#)StatInfo.java, 2018-06-06.
 * <p>
 * Copyright 2018 Stalary.
 */
package com.stalary.usercenter.data.vo;

import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * StatVo
 *
 * @author lirongqian
 * @since 2018/06/06
 */
@Data
@AllArgsConstructor
public class StatVo {

    private Stat stat;

    private UserVo userVo;
}