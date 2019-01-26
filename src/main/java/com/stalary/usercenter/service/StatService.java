package com.stalary.usercenter.service;

import com.stalary.usercenter.data.Constant;
import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.dto.Address;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.data.vo.StatVo;
import com.stalary.usercenter.data.vo.UserVo;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.StatRepo;
import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StatisticsService
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Service
@Slf4j
public class StatService extends BaseService<Stat, StatRepo> {

    @Resource
    private ProjectService projectService;

    @Resource
    private UserService userService;

    @Autowired
    protected StatService(StatRepo statRepo) {
        super(statRepo);
    }

    public void saveUserStat(UserStat userStat) {
        // 查找是否已存在统计，当已存在时更新城市和登陆次数
        Stat stat = repo.findByUserId(userStat.getUserId());
        String city = userStat.getCity();
        if (stat != null) {
            stat.setLoginCount(stat.getLoginCount() + 1);
            List<Address> addressList = stat.getCityList();
            boolean flag = true;
            for (Address address : addressList) {
                if (address.getAddress().equals(city)) {
                    address.setCount(address.getCount() + 1);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                addressList.add(new Address(city, 1));
            }
            stat.setCityList(addressList);
            repo.save(stat);
        } else {
            Stat newStat = new Stat();
            newStat.setUserId(userStat.getUserId());
            newStat.setLoginCount(1L);
            List<Address> addressList = new ArrayList<>();
            addressList.add(new Address(city, 1));
            newStat.setCityList(addressList);
            repo.save(newStat);
        }
    }

    public List<StatVo> findByProjectId(Long projectId, String key) {
        // 验证密钥
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        List<Long> userIdList = userService
                .findByProjectId(projectId)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
        log.info(UCUtil.genLog(Constant.USER_LOG, Constant.PROJECT, projectId, "查看统计信息"));
        return repo.findStatList(userIdList)
                .stream()
                .map(stat -> {
                    stat.getCityList();
                    User one = userService.findOne(stat.getUserId());
                    return new StatVo(stat, new UserVo(one.getId(), one.getUsername(), one.getRole(), one.getCreateTime()));
                })
                .collect(Collectors.toList());
    }

    public Stat findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }
}