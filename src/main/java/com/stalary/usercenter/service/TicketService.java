package com.stalary.usercenter.service;

import com.stalary.usercenter.data.entity.Ticket;
import com.stalary.usercenter.repo.BaseRepo;
import com.stalary.usercenter.repo.TicketRepo;
import org.springframework.stereotype.Service;

/**
 * TicketService
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Service
public class TicketService extends BaseService<Ticket, TicketRepo> {

    public TicketService(TicketRepo repo) {
        super(repo);
    }
}