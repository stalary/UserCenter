
package com.stalary.usercenter.service.kafka;

import com.google.gson.Gson;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.service.LogService;
import com.stalary.usercenter.service.StatService;
import com.stalary.usercenter.utils.UCUtil;
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

    public static final String LOG = "center_log";

    @Resource
    private Gson gson;

    @Resource
    private StatService statService;

    @Resource
    private LogService logService;

    @KafkaListener(topics = {LOGIN_STAT, LOG})
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
        } else if (LOG.equals(topic)) {
            String[] split = message.split(UCUtil.SPLIT);
            String level = split[0];
            String type = split[2];
            Long commonId = Long.valueOf(split[3]);
            String content = split[4];
            // 异步存储日志
            Log oldLog = logService.findOldLog(commonId, type, content);
            if (oldLog != null) {
                oldLog.setCount(oldLog.getCount() + 1);
                logService.save(oldLog);
            } else {
                Log log = new Log(level, content, type, commonId, 1);
                logService.save(log);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
