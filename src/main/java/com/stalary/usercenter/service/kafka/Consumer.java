
package com.stalary.usercenter.service.kafka;

import com.google.gson.Gson;
import com.stalary.lightmqclient.MQListener;
import com.stalary.lightmqclient.MessageDto;
import com.stalary.lightmqclient.facade.MQConsumer;
import com.stalary.usercenter.data.dto.ThreadHolder;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.service.LogService;
import com.stalary.usercenter.service.StatService;
import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Consumer
 *
 * @author lirongqian
 * @since 2018/03/21
 */
@Slf4j
@Component
public class Consumer implements MQConsumer {

    public static final String LOGIN_STAT = "login_stat";

    public static final String LOG = "center_log";

    private static Gson gson;

    private static StatService statService;

    private static LogService logService;

    @Autowired
    public void setGson(Gson gson) {
        Consumer.gson = gson;
    }

    @Autowired
    public void setStatService(StatService statService) {
        Consumer.statService = statService;
    }

    @Autowired
    public void setLogService(LogService logService) {
        Consumer.logService = logService;
    }

    public static Map<String, ThreadHolder> map = new HashMap<>();

    @Override
    @MQListener(topics = {LOGIN_STAT, LOG, "test"})
    public void process(MessageDto messageDto) {
        try {
            long startTime = System.currentTimeMillis();
            String topic = messageDto.getTopic();
            String message = messageDto.getValue();
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
                if (StringUtils.isNotEmpty(level)
                        && StringUtils.isNotEmpty(type)
                        && StringUtils.isNotEmpty(content)) {
                    Log oldLog = logService.findOldLog(commonId, type, content);
                    if (oldLog != null) {
                        oldLog.setCount(oldLog.getCount() + 1);
                        logService.save(oldLog);
                    } else {
                        Log log = new Log(level, content, type, commonId, 1);
                        logService.save(log);
                    }
                }
            } else if ("test".equals(topic)) {
                log.info("receive message: " + message);
            }
            long endTime = System.currentTimeMillis();
            log.info("SubmitConsumer.time=" + (endTime - startTime));
        } catch (Exception e) {
            log.warn("light consumer error", e);
        }
    }
}
