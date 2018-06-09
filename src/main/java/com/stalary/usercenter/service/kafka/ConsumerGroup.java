package com.stalary.usercenter.service.kafka;

import com.stalary.usercenter.data.dto.ThreadHolder;
import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ConsumerGroup
 *
 * @author lirongqian
 * @since 2018/03/22
 */
@Slf4j
@Component
public class ConsumerGroup {

    private ExecutorService executor;

    @Resource
    private Consumer consumer;

    public static Map<String, ThreadHolder> map = new HashMap<>();

    public ConsumerGroup(
            @Value("${consumer.concurrency}") int concurrency,
            @Value("${spring.kafka.bootstrap-servers}") String servers,
            @Value("${consumer.group-id}") String group,
            @Value("${consumer.topic}") String topics) {
        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", servers);
        config.put("group.id", group);
        config.put("enable.auto.commit", false);
        config.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        Map<String, Integer> topicMap = new HashMap<>();
        String[] topicList = topics.split(",");
        for (String topic : topicList) {
            topicMap.put(topic, concurrency);
        }
        if (concurrency >= 1) {
            this.executor = Executors.newFixedThreadPool(concurrency * topicList.length);
            int threadNum = 0;
            for (String topic : topicMap.keySet()) {
                executor.submit(new ConsumerThread(config, topic, ++threadNum));
            }
        }
    }

    public class ConsumerThread implements Runnable {

        /**
         * 每个线程私有的KafkaConsumer实例
          */
        private KafkaConsumer<String, String> kafkaConsumer;

        private int id;

        private String name;

        public ConsumerThread(Map<String, Object> consumerConfig, String topic, int threadId) {
            this.id = threadId;
            this.name = topic;
            Properties props = new Properties();
            props.putAll(consumerConfig);
            this.kafkaConsumer = new KafkaConsumer<>(props);
            kafkaConsumer.subscribe(Collections.singletonList(topic));
        }

        @Override
        public void run() {
            log.info("consumer task start, id = " + id);
            try {
                while (true) {
                    // 构建kafka监控
                    Thread thread = Thread.currentThread();
                    ThreadHolder threadHolder = new ThreadHolder(this.id, this.name, thread.getState());
                    map.put(UCUtil.KAFKA_INFO, threadHolder);
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
                    for (ConsumerRecord<String, String> record : records) {
                        int partition = record.partition();
                        long offset = record.offset();
                        String key = record.key();
                        String value = record.value();
                        log.info(String.format("partition:%d, offset:%d, key:%s, message:%s", partition, offset, key, value));
                        consumer.process(record);
                        kafkaConsumer.commitAsync();
                    }
                }
            } catch (Exception e) {
                log.warn("process message failure!", e);
            } finally {
                kafkaConsumer.close();
                ThreadHolder threadHolder = new ThreadHolder(this.id, this.name, Thread.State.TERMINATED);
                map.put(UCUtil.KAFKA_INFO, threadHolder);
                log.info("consumer task shutdown, id = " + id);
            }
        }
    }
}