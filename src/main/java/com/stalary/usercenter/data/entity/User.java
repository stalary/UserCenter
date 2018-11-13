package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @model User
 * @description 用户对象
 * @field username 用户名
 * @field nickname 昵称(非必须)
 * @field email 邮箱(非必须)
 * @field phone 手机号(非必须)
 * @field avatar 头像(非必须)
 * @field password 密码
 * @field projectId 项目id
 * @field remember 是否记住密码
 * @field role 默认为0，代表最低普通用户，其他规则调用方自行构建
 * @field firstId 预留的绑定id字段
 * @field secondId 预留的绑定id字段
 * @field thirdId 预留的绑定id字段
 * @field status 状态，-1为删除，0为正常，默认为0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
@Entity
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
    private String password;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 项目id
     */
    private Long projectId;

    @Transient
    private boolean remember = false;

    /**
     * 默认为0，代表最低普通用户，其他规则调用方自行构建
     */
    private Integer role = 0;

    /**
     * 预留的绑定id字段
     */
    private Long firstId;

    private Long secondId;

    private Long thirdId;

    /**
     * 状态，-1为删除，0为正常，默认为0
     */
    @JsonIgnore
    private Integer status = 0;
}