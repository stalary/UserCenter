package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * User
 *
 * @author lirongqian
 * @since 2018/03/24
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称(非必须)
     */
    private String nickname;

    /**
     * 邮箱(非必须)
     */
    private String email;

    /**
     * 手机号(非必须)
     */
    private String phone;

    /**
     * 头像(非必须)
     */
    private String avatar;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 项目id
     */
    @JsonIgnore
    private long projectId;

}