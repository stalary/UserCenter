package com.stalary.usercenter.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.utils.UCUtil;

/**
 * MessageFormatter
 * 日志规则为 user_log:type:id:content
 * @author lirongqian
 * @since 2018/04/02
 */
public class MessageFormatter implements Formatter {
    @Override
    public String format(ILoggingEvent event) {
        if (event.getFormattedMessage().startsWith(UCUtil.USER_LOG)) {
            return event.getLevel().toString() + UCUtil.SPLIT + event.getFormattedMessage();
        } else {
            return null;
        }
    }
}