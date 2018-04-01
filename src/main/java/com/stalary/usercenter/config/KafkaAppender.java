package com.stalary.usercenter.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * KafkaAppender
 *
 * @author lirongqian
 * @since 2018/04/02
 */
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private Formatter formatter;


    @Override
    public void start() {
        if(this.formatter == null){
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
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaTemplate = new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory<String, String>(props));
        System.out.println(1213);
        System.out.println(event);
        String payload = this.formatter.format(event);
        ListenableFuture<SendResult<String, String>> test = kafkaTemplate.send("test","", payload);
        try {
            System.out.println(test.get().getRecordMetadata());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
