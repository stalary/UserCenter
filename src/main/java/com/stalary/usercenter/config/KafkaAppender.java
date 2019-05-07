package com.stalary.usercenter.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.stalary.lightmqclient.LightMQProperties;
import com.stalary.lightmqclient.WebClientService;
import com.stalary.lightmqclient.facade.Producer;
import com.stalary.usercenter.service.lightmq.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * KafkaAppender
 *
 * @author lirongqian
 * @since 2018/04/02
 */
@Slf4j
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

    @Resource
    private Formatter formatter;

    Producer producer;

    @Override
    public void start() {
        if (this.formatter == null) {
            this.formatter = new MessageFormatter();
        }
        LightMQProperties properties = new LightMQProperties();
        properties.setUrl("http://lightmq.stalary.com");
        WebClientService service = new WebClientService(properties);
        producer = new Producer(service);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent event) {
        String logStr = this.formatter.format(event);
        if (logStr != null) {
            producer.send(Consumer.LOG, logStr);
            log.info("send message: topic: " + Consumer.LOG + " message: " + logStr);
        }
    }

}
