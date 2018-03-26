package com.stalary.usercenter.data;

import lombok.Getter;

/**
 * @author Stalary
 * @description
 * @date 2018/03/24
 */
public enum ResultEnum {
    UNKNOW_ERROR(-1000, "未知错误"),
    // 1开头为用户有关的错误
    USERNAME_EMPTY(1001, "用户名为空"),
    PASSWORD_EMPTY(1002, "密码为空"),
    USERNAME_PASSWORD_ERROR(1003, "账号密码错误"),
    REPEAT_REGISTER(1003, "该用户已注册"),

    SUCCESS(0, "成功");

    @Getter
    private Integer code;

    @Getter
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
