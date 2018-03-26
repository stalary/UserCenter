package com.stalary.usercenter.repo;

import com.stalary.usercenter.data.entity.Project;

/**
 * @author Stalary
 * @description
 * @date 2018/3/27
 */
public interface ProjectRepo extends BaseRepo<Project, Long> {

    Project findByNameAndStatusGreaterThanEqual(String name, Integer status);
}
