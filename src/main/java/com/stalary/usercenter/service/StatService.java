package com.stalary.usercenter.service;

import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.repo.StatRepo;
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
        Stat stat = repo.findByUserId(userStat.getUserId());
        String city = userStat.getCity();
        if (stat != null) {
            stat.setLoginCount(stat.getLoginCount() + 1);
            stat.setCity(userStat.getCity());
            Map<String, Integer> cityMap = stat.getCityMap();
            cityMap.put(city, cityMap.getOrDefault(city, 0) + 1);
            Map<String, Integer> sortMap = stat.sort(cityMap);
            stat.setCityMap(sortMap);
            stat.serializeFields();
            repo.save(stat);
        } else {
            Stat newStat = new Stat();
            newStat.setUserId(userStat.getUserId());
            newStat.setLoginCount(1L);
            Map<String, Integer> cityMap = new HashMap<>();
            cityMap.put(city, 1);
            Map<String, Integer> sortMap = newStat.sort(cityMap);
            newStat.setCityMap(sortMap);
            newStat.serializeFields();
            repo.save(newStat);
        }
    }

    public Stat findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }
}