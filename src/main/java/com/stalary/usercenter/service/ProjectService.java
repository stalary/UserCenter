/**
 * @(#)ProjectService.java, 2018-03-27.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.service;

import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.entity.Project;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProjectService
 *
 * @author lirongqian
 * @since 2018/03/27
 */
@Service
public class ProjectService extends BaseService<Project, ProjectRepo> {

    @Autowired
    protected ProjectService(ProjectRepo repo) {
        super(repo);
    }

    public Long save(String name) {
        Project project = repo.findByNameAndStatusGreaterThanEqual(name, 0);
        if (project != null) {
            throw new MyException(ResultEnum.PROJECT_REPEAT);
        }
        project = new Project();
        project.setName(name);
        repo.save(project);
        return project.getId();
    }

    public Long get(String name) {
        Project project = repo.findByNameAndStatusGreaterThanEqual(name, 0);
        if (project == null) {
            throw new MyException(ResultEnum.PROJECT_ERROR);
        }
        return project.getId();
    }
}