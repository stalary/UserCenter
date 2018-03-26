package com.stalary.usercenter.service;

import com.stalary.usercenter.data.dto.Address;
import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.repo.StatRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StatisticsService
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Service
@Slf4j
public class StatService extends BaseService<Stat, StatRepo> {

    @Autowired
    protected StatService(StatRepo repo) {
        super(repo);
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

    public Stat findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }
}