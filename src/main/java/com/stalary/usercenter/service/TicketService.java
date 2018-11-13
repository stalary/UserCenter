package com.stalary.usercenter.service;

import com.stalary.usercenter.data.entity.Ticket;
import com.stalary.usercenter.repo.BaseRepo;
import com.stalary.usercenter.repo.TicketRepo;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * TicketService
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Service
public class TicketService extends BaseService<Ticket, TicketRepo> {

    public TicketService(TicketRepo ticketRepo) {
        super(ticketRepo);
    }

    public Ticket findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    /**
     * 判断ticket是否过期
     * @param userId
     * @return
     */
    public boolean judgeTime(Long userId) {
        Ticket ticket = repo.findByUserId(userId);
        if (ticket.getExpired().getTime() >= (System.currentTimeMillis())) {
            return true;
        }
        return false;
    }
}