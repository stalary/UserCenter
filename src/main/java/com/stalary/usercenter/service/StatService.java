package com.stalary.usercenter.service;

import com.stalary.usercenter.data.dto.UserStat;
import com.stalary.usercenter.data.entity.Stat;
import com.stalary.usercenter.repo.StatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (stat != null) {
            stat.setLoginCount(stat.getLoginCount() + 1);
            stat.setIp(userStat.getIp());
            stat.setCity(userStat.getCity());
            repo.save(stat);
        } else {
            Stat newStat = new Stat();
            newStat.setUserId(userStat.getUserId());
            newStat.setLoginCount(1L);
            newStat.setIp(userStat.getIp());
            newStat.setCity(userStat.getCity());
            repo.save(newStat);
        }
    }

    public Stat findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }
}