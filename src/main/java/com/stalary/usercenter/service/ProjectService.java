
package com.stalary.usercenter.service;

import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.dto.ProjectInfo;
import com.stalary.usercenter.data.entity.Project;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.ProjectRepo;
import com.stalary.usercenter.utils.PasswordUtil;
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

    public ProjectInfo save(String name) {
        Project project = repo.findByNameAndStatusGreaterThanEqual(name, 0);
        if (project != null) {
            throw new MyException(ResultEnum.PROJECT_REPEAT);
        }
        project = new Project();
        project.setName(name);
        String uuid = PasswordUtil.get10UUID();
        project.setKey(uuid);
        repo.save(project);
        return new ProjectInfo(project.getId(), uuid);
    }

    public ProjectInfo get(String name) {
        Project project = repo.findByNameAndStatusGreaterThanEqual(name, 0);
        if (project == null) {
            throw new MyException(ResultEnum.PROJECT_ERROR);
        }
        return new ProjectInfo(project.getId(), project.getKey());
    }

    /**
     * 验证密钥
     * @return
     */
    public boolean verify(Long id, String key) {
        if (repo.findByIdAndKeyAndStatusGreaterThanEqual(id, key, 0) == null) {
            return false;
        }
        return true;
    }
}