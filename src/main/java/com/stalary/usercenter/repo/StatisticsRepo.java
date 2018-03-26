package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Statistics;

/**
 * @author Stalary
 * @description
 * @date 2018/3/26
 */
public interface StatisticsRepo extends BaseRepo<Statistics, Long> {

    /**
     * 通过用户id查找统计信息
     * @param userId
     * @return
     */
    Statistics findByUserId(Long userId);
}
