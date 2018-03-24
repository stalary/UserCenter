package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * BaseEntity
 *
 * @author lirongqian
 * @since 2018/03/24
 */
@Data
public abstract class BaseEntity {

    /**
     * 自增id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 状态，-1为删除，0为正常
     */
    @JsonIgnore
    private Integer status;

    /**
     * 最后更新时间
     */
    @JsonIgnore
    @UpdateTimestamp
    private Date updateTime;

    /**
     * 创建时间
     */
    @CreationTimestamp
    private Date createTime;
}