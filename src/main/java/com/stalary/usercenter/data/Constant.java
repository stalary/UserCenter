package com.stalary.usercenter.data;

/**
 * Constant
 * 用户中心的工具类，包括常量的存放
 *
 * @author lirongqian
 * @since 2018/03/25
 */
public class Constant {

    public static final String SPLIT = ":";

    public static final String USER_LOG = "user_log";

    public static final String USER = "user";

    public static final String PROJECT = "project";

    public static final String MAIL = "mail";

    public static final String HTTP = "http";

    public static final String KAFKA_INFO = "kafka_info";

    public static final String INFO_LOG = "INFO";

    public static final String WARN_LOG = "WARN";

    public static final String ERROR_LOG = "ERROR";

    /** 配置支持跨域列表 **/
    public static final String[] ORIGIN = {
            "http://localhost:7300",
            "http://120.24.5.178:7300",
            "http://47.94.248.38:7300",
    "http://userfe.stalary.com"};

}