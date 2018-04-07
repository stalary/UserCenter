package com.stalary.usercenter.service;

import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MailService
 *
 * @author wangshuguang
 * @since 2018/03/31
 */
@Service
@Slf4j
public class MailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String sender;

    /**
     * 发送不带附件的简单邮件，用于提示账号登录异常
     * 邮件由于不存在id，所以默认id设置为-1
     * @param receiver 收件人
     */
    public void sendSimpleMail(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("异地登录警告");
        message.setText("检测到您的账号存在异地登录，请确定是否为您本人操作，如果非本人操作，则账号存在被盗风险！");
        try {
            mailSender.send(message);
            log.info("user_log" + UCUtil.SPLIT + UCUtil.MAIL + UCUtil.SPLIT + -1 + UCUtil.SPLIT + "异地警报邮件发送成功");
        } catch (MailException e) {
            log.warn("user_log" + UCUtil.SPLIT + UCUtil.MAIL + UCUtil.SPLIT + -1 + UCUtil.SPLIT + "异地警报邮件发送失败");
        }
    }
}
