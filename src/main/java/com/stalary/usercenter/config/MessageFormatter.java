package com.stalary.usercenter.config;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * MessageFormatter
 *
 * @author lirongqian
 * @since 2018/04/02
 */
public class MessageFormatter implements Formatter {
    @Override
    public String format(ILoggingEvent event) {
        return event.getFormattedMessage();
    }
}