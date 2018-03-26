package com.stalary.usercenter.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * UserStat
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Data
@AllArgsConstructor
public class UserStat {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登陆的城市
     */
    private String city;

    /**
     * 最近一次登陆的时间
     */
    private Date lateLoginTime;
}