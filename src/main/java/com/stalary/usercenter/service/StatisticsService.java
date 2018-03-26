package com.stalary.usercenter.service;

import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Statistics;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.repo.StatisticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * StatisticsService
 *
 * @author lirongqian
 * @since 2018/03/26
 */
@Service
public class StatisticsService extends BaseService<Statistics, StatisticsRepo> {

    @Autowired
    protected StatisticsService(StatisticsRepo repo) {
        super(repo);
    }

    public void saveUserStat(UserStat userStat) {
        Statistics stat = repo.findByUserId(userStat.getUserId());
        if (stat != null) {
            stat.setLoginCount(stat.getLoginCount() + 1);
            stat.setIp(userStat.getIp());
            stat.setCity(userStat.getCity());
            repo.save(stat);
        } else {
            Statistics newStat = new Statistics();
            newStat.setUserId(userStat.getUserId());
            newStat.setLoginCount(1L);
            newStat.setIp(userStat.getIp());
            newStat.setCity(userStat.getCity());
            repo.save(newStat);
        }
    }
}