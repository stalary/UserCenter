package com.stalary.usercenter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Stalary
 * @description
 * @date 2018/11/4
 */
@FeignClient(name = "out", url = "http://whois.pconline.com.cn/ipJson.jsp")
@Component
public interface OutClient {

    @RequestMapping("/ip")
    String getIp(@RequestParam("ip") String ip);
}
