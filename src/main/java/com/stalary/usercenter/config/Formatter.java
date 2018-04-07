package com.stalary.usercenter.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.stalary.usercenter.data.entity.Log;

/**
 * @author Stalary
 * @description
 * @date 2018/4/2
 */
public interface Formatter {

    String format(ILoggingEvent event);
}
