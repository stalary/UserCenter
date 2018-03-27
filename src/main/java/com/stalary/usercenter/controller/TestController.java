package com.stalary.usercenter.controller;

import com.stalary.usercenter.data.dto.ResponseMessage;
import com.stalary.usercenter.service.kafka.Producer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TestController
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Api(value = "测试controller", tags = "用于测试")
@RequestMapping("/test")
@RestController
@Slf4j
public class TestController {

    @Resource
    private Producer producer;

    @GetMapping("/send")
    public ResponseMessage testKafka(
            @RequestParam String message) {
        producer.send("test", message);
        return ResponseMessage.successMessage();
    }
}