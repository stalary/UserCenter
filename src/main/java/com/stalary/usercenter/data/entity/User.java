package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户对象")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 昵称(非必须)
     */
    @ApiModelProperty("昵称(非必须)")
    private String nickname;

    /**
     * 邮箱(非必须)
     */
    @ApiModelProperty("邮箱(非必须)")
    private String email;

    /**
     * 手机号(非必须)
     */
    @ApiModelProperty("手机号(非必须)")
    private String phone;

    /**
     * 头像(非必须)
     */
    @ApiModelProperty("头像(非必须)")
    private String avatar;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 项目id
     */
    @ApiModelProperty("项目编号(用户中心进行分发)")
    private Long projectId;

    @Transient
    @ApiModelProperty("是否记住密码")
    private boolean remember = false;

    /**
     * 默认为0，代表最低普通用户，其他规则调用方自行构建
     */
    @ApiModelProperty("角色")
    private Integer role;

    /**
     * 状态，-1为删除，0为正常，默认为0
     */
    @JsonIgnore
    private Integer status = 0;
}