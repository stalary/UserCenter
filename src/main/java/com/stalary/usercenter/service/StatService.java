package com.stalary.usercenter.service;

import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.repo.StatRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * StatisticsService
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Service
public class StatService extends BaseService<Stat, StatRepo> {

    @Autowired
    protected StatService(StatRepo repo) {
        super(repo);
    }

    public void saveUserStat(UserStat userStat) {
        // 查找是否已存在统计，当已存在时更新城市和登陆次数
        Stat stat = repo.findByUserId(userStat.getUserId());
        String city = StringUtils.isBlank(userStat.getCity()) ? "济南" : userStat.getCity();
        if (stat != null) {
            stat.setLoginCount(stat.getLoginCount() + 1);
            Map<String, Integer> cityMap = stat.getCityMap();
            cityMap.put(city, cityMap.getOrDefault(city, 0) + 1);
            stat.setCityMap(cityMap);
            repo.save(stat);
        } else {
            Stat newStat = new Stat();
            newStat.setUserId(userStat.getUserId());
            newStat.setLoginCount(1L);
            Map<String, Integer> cityMap = new HashMap<>();
            cityMap.put(city, 1);
            newStat.setCityMap(cityMap);
            repo.save(newStat);
        }
    }

    public Stat findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }
}