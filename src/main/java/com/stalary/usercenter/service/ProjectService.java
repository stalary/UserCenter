
package com.stalary.usercenter.service;

import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.dto.ProjectInfo;
import com.stalary.usercenter.data.entity.Project;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.ProjectRepo;
import com.stalary.usercenter.utils.PasswordUtil;
import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProjectService
 *
 * @author lirongqian
 * @since 2018/03/27
 */
@Service
@Slf4j
public class ProjectService extends BaseService<Project, ProjectRepo> {

    @Autowired
    protected ProjectService(ProjectRepo repo) {
        super(repo);
    }

    public ProjectInfo save(String name, String phone) {
        Project project = repo.findByNameAndPhoneAndStatusGreaterThanEqual(name, phone, 0);
        if (project != null) {
            throw new MyException(ResultEnum.PROJECT_REPEAT);
        }
        project = new Project();
        project.setName(name);
        project.setPhone(phone);
        String uuid = PasswordUtil.get10UUID();
        project.setKey(uuid);
        repo.save(project);
        return new ProjectInfo(project.getId(), uuid);
    }

    public ProjectInfo get(String name, String phone) {
        Project project = repo.findByNameAndPhoneAndStatusGreaterThanEqual(name, phone,  0);
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
            log.warn("user_log" + UCUtil.SPLIT + UCUtil.PROJECT + UCUtil.SPLIT + id + UCUtil.SPLIT + "项目验证密钥" + key + "失败");
            return false;
        }
        return true;
    }
}