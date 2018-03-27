/**
 * @(#)ProjectInfo.java, 2018-03-27.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProjectInfo
 *
 * @author lirongqian
 * @since 2018/03/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfo {

    private Long projectId;

    private String key;
}