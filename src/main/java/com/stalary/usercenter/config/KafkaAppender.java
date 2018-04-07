package com.stalary.usercenter.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.service.kafka.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * KafkaAppender
 *
 * @author lirongqian
 * @since 2018/04/02
 */
@Slf4j
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private Formatter formatter;

    @Resource
    private Gson gson;


    @Override
    public void start() {
        if (this.formatter == null) {
            this.formatter = new MessageFormatter();
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent event) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://47.94.248.38:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<String, String>(props));
        String logStr = this.formatter.format(event);
        if (logStr != null) {
            ListenableFuture<SendResult<String, String>> test = kafkaTemplate.send(Consumer.LOG, logStr);
            log.info("send message: topic: " + Consumer.LOG + " message: " + gson.toJson(log));
            try {
                System.out.println(test.get().getRecordMetadata());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
