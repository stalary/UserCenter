package com.stalary.usercenter.service;

import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.repo.LogRepo;
import com.stalary.usercenter.utils.UCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LogService
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Service
public class LogService extends BaseService<Log, LogRepo> {

    @Resource
    private ProjectService projectService;

    @Resource
    private UserService userService;

    @Autowired
    public LogService(LogRepo repo) {
        super(repo);
    }

    public Log findOldLog(Long commonId, String type, String content) {
        return repo.findByCommonIdAndTypeAndContent(commonId, type, content);
    }

    public List<Log> findProjectLog(String key, Long projectId) {
        if (projectService.verify(projectId, key)) {
            List<Long> userIdList = userService
                    .findByProjectId(projectId)
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            // 查找用户日志
            List<Log> userLog =
                    repo
                    .findLogByProject(UCUtil.USER, userIdList)
                    .stream()
                    .peek(log -> log.setContent(UCUtil.USER + UCUtil.SPLIT + log.getCommonId() + log.getContent()))
                    .collect(Collectors.toList());
            // 查找项目日志
            List<Log> projectLog = repo
                    .findLogByProject(UCUtil.PROJECT, Collections.singletonList(projectId))
                    .stream()
                    .peek(log -> log.setContent(UCUtil.PROJECT + UCUtil.SPLIT + log.getCommonId() + log.getContent()))
                    .collect(Collectors.toList());
            // 合并日志
            userLog.addAll(projectLog);
            return userLog;
        }
        return new ArrayList<>();
    }
}