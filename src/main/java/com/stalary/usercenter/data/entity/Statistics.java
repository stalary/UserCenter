package com.stalary.usercenter.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * UserStat
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "statistics")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics extends BaseEntity {

    /**
     * 关联的用户id
     */
    private Long userId;

    /**
     * 登陆次数
     */
    private Long loginCount;

    /**
     * 最近一次登陆的时间
     */
    private Date lateLoginTime;

}