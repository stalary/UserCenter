package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Project;

/**
 * @author Stalary
 * @description
 * @date 2018/3/27
 */
public interface ProjectRepo extends BaseRepo<Project, Long> {

    Project findByNameAndPhoneAndStatusGreaterThanEqual(String name, String phone,  Integer status);

    Project findByIdAndKeyAndStatusGreaterThanEqual(Long id, String key, Integer status);
}
