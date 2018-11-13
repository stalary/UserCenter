package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Project;
import org.springframework.stereotype.Repository;

/**
 * @author Stalary
 * @description
 * @date 2018/3/27
 */
@Repository
public interface ProjectRepo extends BaseRepo<Project, Long> {

    Project findByNameAndPhoneAndStatusGreaterThanEqual(String name, String phone,  Integer status);

    Project findByIdAndKeyAndStatusGreaterThanEqual(Long id, String key, Integer status);
}
