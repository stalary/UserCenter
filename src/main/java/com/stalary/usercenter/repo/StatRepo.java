package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Stat;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Stalary
 * @description
 * @date 2018/3/26
 */
public interface StatRepo extends BaseRepo<Stat, Long> {

    /**
     * 通过用户id批量查找统计信息
     * @param userId
     * @return
     */
    @Query("select s from Stat s where s.userId in ?1")
    List<Stat> findStatList(List<Long> userId);

    /**
     * 通过用户id查找统计信息
     * @param userId
     * @return
     */
    Stat findByUserId(Long userId);
}
