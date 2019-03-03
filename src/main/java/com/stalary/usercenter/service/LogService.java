package com.stalary.usercenter.service;

import com.stalary.usercenter.data.Constant;
import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.entity.Log;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.exception.MyException;
import com.stalary.usercenter.repo.LogRepo;
import com.stalary.usercenter.utils.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LogService
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Service
@Slf4j
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

    public Map<String, List<Log>> findProjectLog(String key, Long projectId) {
        List<Log> infoLog = new ArrayList<>();
        List<Log> warnLog = new ArrayList<>();
        List<Log> errorLog = new ArrayList<>();
        if (!projectService.verify(projectId, key)) {
            throw new MyException(ResultEnum.PROJECT_REJECT);
        }
        List<Long> userIdList = userService.getUserIdByProjectId(projectId);
        // 查找用户日志
        List<Log> userLog =
                repo
                        .findLogByProject(Constant.USER, userIdList)
                        .stream()
                        .peek(log -> log.setContent("userId" + Constant.SPLIT + log.getCommonId() + log.getContent()))
                        .collect(Collectors.toList());
        // 查找项目日志
        List<Log> projectLog = repo
                .findLogByProject(Constant.PROJECT, Collections.singletonList(projectId));
        // 合并日志
        userLog.addAll(projectLog);
        userLog.forEach(l -> {
            switch (l.getLevel()) {
                case Constant.INFO_LOG:
                    infoLog.add(l);
                    break;
                case Constant.WARN_LOG:
                    warnLog.add(l);
                    break;
                case Constant.ERROR_LOG:
                    errorLog.add(l);
                    break;
                default:
                    break;
            }
        });
        log.info(UCUtil.genLog(Constant.USER_LOG, Constant.PROJECT, projectId, "查看项目所有日志"));

        Map<String, List<Log>> ret = new HashMap<>();
        ret.put(Constant.INFO_LOG, infoLog);
        ret.put(Constant.WARN_LOG, warnLog);
        ret.put(Constant.ERROR_LOG, errorLog);
        return ret;
    }
}