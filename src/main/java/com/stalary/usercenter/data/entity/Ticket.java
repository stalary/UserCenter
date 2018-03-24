package com.stalary.usercenter.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

/**
 * Ticket
 *
 * @author lirongqian
 * @since 2018/03/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "ticket")
public class Ticket extends BaseEntity {

    /**
     * 关联的用户id
     */
    private Long userId;

    /**
     * ticket内容
     */
    private String ticket;

    /**
     * 到期时间
     */
    private Date expired;
}