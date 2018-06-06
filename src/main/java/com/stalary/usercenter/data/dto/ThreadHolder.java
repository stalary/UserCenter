/**
 * @(#)ThreadHolder.java, 2018-05-22.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ThreadHolder
 *
 * @author lirongqian
 * @since 2018/05/22
 */
@Data
@AllArgsConstructor
public class ThreadHolder {

    /**
     * 线程id
     */
    private Long id;

    /**
     * 线程名称
     */
    private String name;

    /**
     * 线程状态
     */
    private Thread.State state;
}