package com.stalary.usercenter.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HttpResult
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应体
     */
    private String body;

}