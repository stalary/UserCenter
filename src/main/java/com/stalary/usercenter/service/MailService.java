package com.stalary.usercenter.service;

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
     *
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
            log.info("邮件发送成功");
        } catch (MailException e) {
            log.error("邮件发送失败：" + e.getMessage());
        }
    }
}
