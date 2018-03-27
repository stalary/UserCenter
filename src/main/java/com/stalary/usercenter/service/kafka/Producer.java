
package com.stalary.usercenter.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Sender
 *
 * @author lirongqian
 * @since 2018/03/21
 */
@Slf4j
@Component
public class Producer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;


    public void send(String topic, String message) {
        log.info("send message: topic: " + topic + " message: " + message);
        kafkaTemplate.send(topic, message);
    }

    public void send(String topic, String key, String message) {
        log.info("send message: topic: " + topic + " key: " + key + " message: " + message);
        kafkaTemplate.send(topic, key, message);
    }
}