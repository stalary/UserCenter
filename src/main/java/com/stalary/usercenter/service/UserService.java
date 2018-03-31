package com.stalary.usercenter.service;

import com.google.gson.Gson;
import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * UserService
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserService extends BaseService<User, UserRepo> {

    @Resource
    private Producer producer;

    @Resource
    private Gson gson;

    @Resource
    private TicketService ticketService;

    @Resource
    private HttpService httpService;

    @Resource
    private StatService statService;

    @Resource
    private ProjectService projectService;

    @Autowired
    protected UserService(UserRepo repo) {
        super(repo);
    }

    /**
     * 返回token的注册
     *
     * @param user
     * @return
     */
    public String register(User user, HttpServletRequest request, String key) {
        if (!projectService.verify(user.getProjectId(), key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        // 用户名为空
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new MyException(ResultEnum.USERNAME_EMPTY);
        }
        // 密码为空
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new MyException(ResultEnum.PASSWORD_EMPTY);
        }
        // 重复注册
        if (repo.findByUsernameAndProjectIdAndStatusGreaterThanEqual(user.getUsername(), user.getProjectId(), 0) != null) {
            throw new MyException(ResultEnum.USERNAME_REPEAT);
        }
        // 注册用户
        String salt = PasswordUtil.get5UUID();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.getPassword(user.getPassword(), salt));
        repo.save(user);
        // 下发ticket
        Ticket ticket = new Ticket();
        ticket.setUserId(user.getId());
        // 默认失效时间为一天
        ticket.setExpired(TimeUtil.plusDays(new Date(), 1));
        ticket.setTicket(PasswordUtil.get10UUID());
        ticketService.save(ticket);
        // 获取ip和地址
        String ip = httpService.getIp(request);
        String city = httpService.getAddress(ip);
        // 打入消息队列，异步统计
        UserStat userStat = new UserStat(user.getId(), city, new Date());
        producer.send(Consumer.LOGIN_STAT, gson.toJson(userStat));
        // 返回token
        return DigestUtil.Encrypt(user.getId().toString() + UCUtil.SPLIT + user.getProjectId());
    }


    public String login(User user, HttpServletRequest request, String key) {
        if (!projectService.verify(user.getProjectId(), key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        // 用户名为空
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new MyException(ResultEnum.USERNAME_EMPTY);
        }
        // 密码为空
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new MyException(ResultEnum.PASSWORD_EMPTY);
        }
        User oldUser = repo.findByUsernameAndProjectIdAndStatusGreaterThanEqual(user.getUsername(), user.getProjectId(), 0);
        // 用户名错误
        if (oldUser == null) {
            throw new MyException(ResultEnum.USERNAME_PASSWORD_ERROR);
        }
        // 密码错误
        if (!PasswordUtil.getPassword(user.getPassword(), oldUser.getSalt()).equals(oldUser.getPassword())) {
            throw new MyException(ResultEnum.USERNAME_PASSWORD_ERROR);
        }
        // 更新ticket
        Ticket ticket = ticketService.findByUserId(oldUser.getId());
        if (ticket != null) {
            // 默认失效时间为一天，保存密码时保留三十天
            if (user.isRemember()) {
                ticket.setExpired(TimeUtil.plusDays(new Date(), 30));
            } else {
                ticket.setExpired(TimeUtil.plusDays(new Date(), 1));
            }
            ticketService.save(ticket);
        } else {
            ticket = new Ticket();
            // 默认失效时间为一天，保存密码时保留三十天
            if (user.isRemember()) {
                ticket.setExpired(TimeUtil.plusDays(new Date(), 30));
            } else {
                ticket.setExpired(TimeUtil.plusDays(new Date(), 1));
            }
            ticket.setUserId(oldUser.getId());
            ticket.setTicket(PasswordUtil.get10UUID());
            ticketService.save(ticket);
        }
        // 获取ip和地址
        String ip = httpService.getIp(request);
        String city = httpService.getAddress(ip);
        Stat stat = statService.findByUserId(oldUser.getId());
        if (!city.equals(stat.getCityList().get(0).getAddress())) {
            log.warn(user.getUsername() + "异地登陆！" + city);
            // todo:当城市不同时，打入消息队列异步发送警告邮件
        }
        // 打入消息队列，异步统计
        UserStat userStat = new UserStat(oldUser.getId(), city, new Date());
        producer.send(Consumer.LOGIN_STAT, gson.toJson(userStat));
        // 返回token
        return DigestUtil.Encrypt(oldUser.getId().toString() + UCUtil.SPLIT + oldUser.getProjectId());
    }

    public String update(User user, String key) {
        if (!projectService.verify(user.getProjectId(), key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        // 用户名为空
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new MyException(ResultEnum.USERNAME_EMPTY);
        }
        // 密码为空
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new MyException(ResultEnum.PASSWORD_EMPTY);
        }
        User oldUser;
        // 用户名错误
        if (repo.findByUsernameAndProjectIdAndStatusGreaterThanEqual(user.getUsername(), user.getProjectId(), 0) == null) {
            throw new MyException(ResultEnum.USERNAME_PASSWORD_ERROR);
        }
        // 通过手机号或者邮箱修改密码
        if (StringUtils.isNotEmpty(user.getPhone())) {
            oldUser = repo.findByPhoneAndProjectIdAndStatusGreaterThanEqual(user.getPhone(), user.getProjectId(), 0);
        } else if (StringUtils.isNotEmpty(user.getEmail())) {
            oldUser = repo.findByEmailAndProjectIdAndStatusGreaterThanEqual(user.getPhone(), user.getProjectId(), 0);
        } else {
            // 当手机号和邮箱都为空时，无法修改密码
            throw new MyException(ResultEnum.UPDATE_PASSWORD_ERROR);
        }
        if (oldUser == null) {
            throw new MyException(ResultEnum.UPDATE_PASSWORD_ERROR);
        }
        oldUser.setPassword(PasswordUtil.getPassword(user.getPassword(), oldUser.getSalt()));
        repo.save(oldUser);
        // 返回token
        return DigestUtil.Encrypt(oldUser.getId().toString() + UCUtil.SPLIT + oldUser.getProjectId());
    }

    public User findByToken(String token, String key) {
        String decrypt = DigestUtil.Decrypt(token);
        String[] split = decrypt.split(UCUtil.SPLIT);
        long userId = Long.valueOf(split[0]);
        long projectId = Long.valueOf(split[1]);
        // 验证密钥
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        // 查看ticket是否过期
        if (!ticketService.judgeTime(userId)) {
            throw new MyException(ResultEnum.TICKET_EXPIRED);
        }
        return repo.findByIdAndStatusGreaterThanEqual(userId, 0);
    }

    public List<User> findByProjectId(Long projectId) {
        return repo.findByProjectIdAndStatusGreaterThanEqual(projectId, 0);
    }

}