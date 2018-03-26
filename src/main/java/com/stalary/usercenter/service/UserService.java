package com.stalary.usercenter.service;

import com.google.gson.Gson;
import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Ticket;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.UserRepo;
import com.stalary.usercenter.service.kafka.Consumer;
import com.stalary.usercenter.service.kafka.Producer;
import com.stalary.usercenter.utils.DigestUtil;
import com.stalary.usercenter.utils.PasswordUtil;
import com.stalary.usercenter.utils.TimeUtil;
import com.stalary.usercenter.utils.UCUtil;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * UserService
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends BaseService<User, UserRepo> {

    @Resource
    private Producer producer;

    @Resource
    private Gson gson;

    @Resource
    private TicketService ticketService;

    @Resource
    private HttpService httpService;

    @Autowired
    protected UserService(UserRepo repo) {
        super(repo);
    }

    /**
     * 返回token的注册
     * @param user
     * @return
     */
    public String tokenRegister(User user, HttpServletRequest request) {
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new MyException(ResultEnum.USERNAME_EMPTY);
        }
        if (repo.findByUsernameAndProjectIdAndStatusGreaterThanEqual(user.getUsername(), user.getProjectId(), 0) != null) {
            throw new MyException(ResultEnum.REPEAT_REGISTER);
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new MyException(ResultEnum.PASSWORD_ERROR);
        }
        String salt = PasswordUtil.get5UUID();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.getPassword(user.getPassword(), salt));
        repo.save(user);
        Ticket ticket = new Ticket();
        ticket.setUserId(user.getId());
        // 默认失效时间为一天
        ticket.setExpired(TimeUtil.plusDays(new Date(), 1));
        ticket.setTicket(PasswordUtil.get10UUID());
        ticketService.save(ticket);
        String ip = httpService.getIp(request);
        String city = httpService.getAddress(ip);
        UserStat userStat = new UserStat(user.getId(), ip, city, new Date());
        producer.send(Consumer.LOGIN_STAT, gson.toJson(userStat));
        return DigestUtil.Encrypt(user.getId() + UCUtil.SPLIT + user.getProjectId());
    }
}