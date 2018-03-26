package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Stat;

/**
 * @author Stalary
 * @description
 * @date 2018/3/26
 */
public interface StatRepo extends BaseRepo<Stat, Long> {

    /**
     * 通过用户id查找统计信息
     * @param userId
     * @return
     */
    Stat findByUserId(Long userId);
}
