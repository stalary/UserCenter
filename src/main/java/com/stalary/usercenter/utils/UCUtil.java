/**
 * @(#)UCUtil.java, 2019-01-13.
 *
 * Copyright 2019 Stalary.
 */
package com.stalary.usercenter.utils;

import com.stalary.usercenter.data.Constant;

/**
 * UCUtil
 *
 * @author lirongqian
 * @since 2019/01/13
 */
public class UCUtil {

    public static String genLog(Object... content) {
        StringBuilder sb = new StringBuilder();
        for (Object str : content) {
            sb.append(str).append(Constant.SPLIT);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}