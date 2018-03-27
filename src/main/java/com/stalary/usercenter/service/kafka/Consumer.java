/**
 * @(#)Consumer.java, 2018-03-21.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.service.kafka;

import com.google.gson.Gson;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.service.StatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Consumer
 *
 * @author lirongqian
 * @since 2018/03/21
 */
@Slf4j
@Component
public class Consumer {

    public static final String LOGIN_STAT = "login_stat";

    @Resource
    private Gson gson;

    @Resource
    private StatService statService;

    @KafkaListener(topics = {LOGIN_STAT})
    public void process(ConsumerRecord record) {
        long startTime = System.currentTimeMillis();
        String topic = record.topic();
        String key = "";
        if (record.key() != null) {
            key = record.key().toString();
        }

        String message = record.value().toString();
        if (LOGIN_STAT.equals(topic)) {
            UserStat userStat = gson.fromJson(message, UserStat.class);
            statService.saveUserStat(userStat);
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
