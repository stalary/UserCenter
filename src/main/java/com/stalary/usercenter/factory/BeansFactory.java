/**
 * @(#)BeansFactory.java, 2017/02/08.
 * Copyright (c) 2016 Yodao, Inc. All rights reserved.
 * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * BeansFactory
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Component
public class BeansFactory implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BeansFactory.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static Gson getGson() {
        return context.getBean(Gson.class);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

}