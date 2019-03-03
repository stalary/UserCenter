package com.stalary.usercenter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stalary.lightmqclient.facade.Producer;
import com.stalary.usercenter.client.OutClient;
import com.stalary.usercenter.data.Constant;
import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.data.entity.Ticket;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.data.vo.UserVo;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.UserRepo;
import com.stalary.usercenter.service.lightmq.Consumer;
import com.stalary.usercenter.utils.DigestUtil;
import com.stalary.usercenter.utils.PasswordUtil;
import com.stalary.usercenter.utils.TimeUtil;
import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private TicketService ticketService;

    @Resource
    private StatService statService;

    @Resource
    private ProjectService projectService;

    @Resource
    private MailService mailService;

    @Resource
    private OutClient outClient;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    protected UserService(UserRepo userRepo) {
        super(userRepo);
    }

    /** 用于执行一些异步任务 **/
    ExecutorService exec = Executors.newFixedThreadPool(5);

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
        User save = repo.save(user);
        String ip = getIp(request);
        exec.execute(() -> {
            long start = System.currentTimeMillis();
            log.info("start async register task");
            // 下发ticket
            Ticket ticket = new Ticket();
            ticket.setUserId(user.getId());
            // 默认失效时间为一天
            ticket.setExpired(TimeUtil.plusDays(new Date(), 1));
            ticket.setTicket(PasswordUtil.get10UUID());
            ticketService.save(ticket);
            // 获取ip和地址
            String city = getAddress(ip);
            // 打入消息队列，异步统计
            UserStat userStat = new UserStat(user.getId(), city, new Date());
            producer.send(Consumer.LOGIN_STAT, JSONObject.toJSONString(userStat));
            // 缓存7天
            String redisKey = genRedisKey(save.getId());
            redis.opsForValue().set(redisKey, JSONObject.toJSONString(save), 7, TimeUnit.DAYS);
            log.info(UCUtil.genLog(Constant.USER_LOG, Constant.USER, user.getId(), "注册成功成功"));
            log.info("end async register task time=" + (System.currentTimeMillis() - start));
        });
        // 返回token
        return DigestUtil.Encrypt(user.getId().toString() + Constant.SPLIT + user.getProjectId());
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
            throw new MyException(ResultEnum.USERNAME_PASSWORD_ERROR, oldUser.getId());
        }
        String ip = getIp(request);
        // 校验成功后异步执行后续操作
        exec.execute(() -> {
            long start = System.currentTimeMillis();
            log.info("start async login task");
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
            String city = getAddress(ip);
            Stat stat = statService.findByUserId(oldUser.getId());
            // 当无统计信息时，不需要判断异地登陆
            if (stat == null) {
                // 打入消息队列，异步统计
                UserStat userStat = new UserStat(oldUser.getId(), city, new Date());
                producer.send(Consumer.LOGIN_STAT, JSONObject.toJSONString(userStat));
            } else {
                if (!city.equals(stat.getCityList().get(0).getAddress()) && StringUtils.isNotEmpty(user.getEmail())) {
                    log.warn(user.getUsername() + "异地登陆！" + city);
                    // 发送登陆异常的邮件
                    mailService.sendSimpleMail(user.getEmail());
                }
            }
            // 打入消息队列，异步统计
            UserStat userStat = new UserStat(oldUser.getId(), city, new Date());
            producer.send(Consumer.LOGIN_STAT, JSONObject.toJSONString(userStat));
            log.info(UCUtil.genLog(Constant.USER_LOG, Constant.USER, oldUser.getId(), "登陆成功"));
            log.info("end async login task time=" + (System.currentTimeMillis() - start));
        });
        // 返回token
        return DigestUtil.Encrypt(oldUser.getId().toString() + Constant.SPLIT + oldUser.getProjectId());
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
        User oldUser = null;
        // 用户名错误
        if (repo.findByUsernameAndProjectIdAndStatusGreaterThanEqual(user.getUsername(), user.getProjectId(), 0) == null) {
            throw new MyException(ResultEnum.USERNAME_PASSWORD_ERROR);
        }
        // 通过手机号或者邮箱修改密码
        if (StringUtils.isNotEmpty(user.getPhone())) {
            oldUser = repo.findByPhoneAndProjectIdAndStatusGreaterThanEqual(user.getPhone(), user.getProjectId(), 0);
        }
        if (oldUser == null && StringUtils.isNotEmpty(user.getEmail())) {
            oldUser = repo.findByEmailAndProjectIdAndStatusGreaterThanEqual(user.getPhone(), user.getProjectId(), 0);
        }
        if (StringUtils.isNotEmpty(user.getPhone()) && StringUtils.isNotEmpty(user.getEmail())) {
            // 当手机号和邮箱都为空时，无法修改密码
            throw new MyException(ResultEnum.UPDATE_PASSWORD_ERROR);
        }
        if (oldUser == null) {
            throw new MyException(ResultEnum.UPDATE_PASSWORD_ERROR);
        }
        oldUser.setPassword(PasswordUtil.getPassword(user.getPassword(), oldUser.getSalt()));
        repo.save(oldUser);
        // 缓存7天
        String redisKey = genRedisKey(oldUser.getId());
        redis.opsForValue().set(redisKey, JSONObject.toJSONString(oldUser), 7, TimeUnit.DAYS);
        // 返回token
        return DigestUtil.Encrypt(oldUser.getId().toString() + Constant.SPLIT + oldUser.getProjectId());
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @param key
     * @return
     */
    public String updateInfo(User user, String key) {
        // 验证密钥
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
        if (!PasswordUtil.getPassword(user.getPassword(), oldUser.getSalt()).equals(oldUser.getPassword()) && !oldUser.getPassword().equals(user.getPassword())) {
            throw new MyException(ResultEnum.USERNAME_PASSWORD_ERROR, oldUser.getId());
        }
        oldUser.setEmail(user.getEmail());
        oldUser.setFirstId(user.getFirstId());
        oldUser.setSecondId(user.getSecondId());
        oldUser.setThirdId(user.getThirdId());
        oldUser.setNickname(user.getNickname());
        oldUser.setPhone(user.getPhone());
        oldUser.setRole(user.getRole());
        repo.save(oldUser);
        // 缓存7天
        String redisKey = genRedisKey(oldUser.getId());
        redis.opsForValue().set(redisKey, JSONObject.toJSONString(oldUser), 7, TimeUnit.DAYS);
        return DigestUtil.Encrypt(oldUser.getId().toString() + Constant.SPLIT + oldUser.getProjectId());

    }

    public User findByToken(String token, String key) {
        String decrypt = DigestUtil.Decrypt(token);
        log.info("decrypt: " + decrypt);
        String[] split = decrypt.split(Constant.SPLIT);
        long userId = Long.parseLong(split[0]);
        long projectId = Long.parseLong(split[1]);
        // 验证密钥
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        // 查看ticket是否过期
        if (!ticketService.judgeTime(userId)) {
            throw new MyException(ResultEnum.TICKET_EXPIRED, userId);
        }
        return get(userId);
    }

    public User findById(long userId, String key, long projectId) {
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT, projectId);
        }
        return get(userId);
    }

    public List<User> findByProjectId(Long projectId) {
        return repo.findByProjectIdAndStatusGreaterThanEqual(projectId, 0);
    }

    public List<Long> getUserIdByProjectId(Long projectId) {
        return findByProjectId(projectId)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public List<User> findByRole(Long projectId, String key, Integer role) {
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT, projectId);
        }
        return repo.findByProjectIdAndRoleAndStatusGreaterThanEqual(projectId, role, 0);
    }

    private User get(long userId) {
        String redisKey = genRedisKey(userId);
        String redisData = redis.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(redisData)) {
            User user = repo.findByIdAndStatusGreaterThanEqual(userId, 0);
            if (user != null) {
                // 缓存7天
                redis.opsForValue().set(redisKey, JSONObject.toJSONString(user), 7, TimeUnit.DAYS);
            }
            return user;
        }
        return JSONObject.parseObject(redisData, User.class);
    }

    private String genRedisKey(long userId) {
        return Constant.USER_PREFIX + Constant.SPLIT + String.valueOf(userId);
    }

    /**
     * 获取请求者的ip地址
     *
     * @param request
     * @return
     */
    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getAddress(String ip) {
        String address = "济南";
        try {
            address = outClient.getIp(ip);
            Pattern compile = Pattern.compile("(\\{\".*\"\\})");
            Matcher matcher = compile.matcher(address);
            if (matcher.find()) {
                address = matcher.group();
                JSONObject jsonObject = JSONObject.parseObject(address);
                return jsonObject.getString("city");
            } else {
                return "济南";
            }
        } catch (Exception e) {
            log.warn("get address error", e);
        }
        return address;
    }

    public List<UserVo> findProjectUser(Long projectId, String key) {
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        log.info(UCUtil.genLog(Constant.USER_LOG, Constant.PROJECT, projectId, "查看项目所有用户信息"));
        return repo.findByProjectIdAndStatusGreaterThanEqual(projectId, 0)
                .stream()
                .map(u -> new UserVo(u.getId(), u.getUsername(), u.getRole(), u.getCreateTime()))
                .collect(Collectors.toList());
    }
}