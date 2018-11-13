package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Ticket;
import org.springframework.stereotype.Repository;

/**
 * TicketRepo
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Repository
public interface TicketRepo extends BaseRepo<Ticket, Long> {

    Ticket findByUserId(Long userId);
}