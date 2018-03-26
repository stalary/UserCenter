package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Ticket;

/**
 * TicketRepo
 *
 * @author lirongqian
 * @since 2018/03/26
 */
public interface TicketRepo extends BaseRepo<Ticket, Long> {

    Ticket findByUserId(Long userId);
}