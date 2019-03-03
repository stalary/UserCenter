
package com.stalary.usercenter.controller;

import com.stalary.usercenter.data.dto.ResponseMessage;
import com.stalary.usercenter.service.LogService;
import com.stalary.usercenter.service.ProjectService;
import com.stalary.usercenter.service.StatService;
import com.stalary.usercenter.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * FacadeController
 * 提供对外服务的Controller
 * @author lirongqian
 * @since 2018/03/27
 */
@RequestMapping("/facade")
@RestController
public class FacadeController {

    @Resource
    private ProjectService projectService;

    @Resource
    private UserService userService;

    @Resource
    private StatService statService;

    @Resource
    private LogService logService;

    /**
     * @method register 项目注册
     * @param name 项目名
     * @param phone 负责人手机号
     * @return ProjectInfo 生成的项目信息
     **/
    @PostMapping("/project")
    public ResponseMessage register(
            @RequestParam String name,
            @RequestParam String phone) {
        return ResponseMessage.successMessage(projectService.save(name, phone));
    }

    /**
     * @method getAllProject 获取所有项目信息
     * @return ProjectVo
     **/
    @GetMapping("/project/all")
    public ResponseMessage getAllProject() {
        return ResponseMessage.successMessage(projectService.findAll());
    }

    /**
     * @method getInfo 获取项目信息
     * @param name 项目名
     * @param phone 负责人手机号
     * @return ProjectInfo 生成的项目信息
     **/
    @GetMapping("/project")
    public ResponseMessage getInfo(
            @RequestParam String name,
            @RequestParam String phone) {
        return ResponseMessage.successMessage(projectService.get(name, phone));
    }

    /**
     * @method getUser 通过token获取用户信息
     * @param token token
     * @param key 项目的key
     * @return User 用户对象
     **/
    @GetMapping("/token")
    public ResponseMessage getUser(
            @RequestParam String token,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.findByToken(token, key));
    }

    /**
     * @method getAllUserStat 获取当前项目的所有统计信息
     * @param projectId  项目id
     * @param key 项目的key
     **/
    @GetMapping("/statistics")
    public ResponseMessage getAllUserStat(
            @RequestParam Long projectId,
            @RequestParam String key) {
        return ResponseMessage.successMessage(statService.getStatByProjectId(projectId, key));
    }

    /**
     * @method getUserStat 获取指定项目某个用户的统计信息
     * @param projectId 项目id
     * @param key 项目的key
     * @param userId 用户id
     * @return Stat 统计信息
     **/
    @GetMapping("/statistics/user")
    public ResponseMessage getUserStat(
            @RequestParam Long projectId,
            @RequestParam String key,
            @RequestParam Long userId) {
        return ResponseMessage.successMessage(statService.getStatByUserId(projectId, key, userId));
    }

    /**
     * @method getByRole 获取项目中指定角色的所有用户
     * @param projectId 项目id
     * @param key 项目的key
     * @param role 角色id
     * @return User 用户对象
     **/
    @GetMapping("/role")
    public ResponseMessage getByRole(
            @RequestParam Long projectId,
            @RequestParam String key,
            @RequestParam Integer role) {
        return ResponseMessage.successMessage(userService.findByRole(projectId, key, role));
    }

    /**
     * @method getById 通过id获取用户信息
     * @param userId 用户id
     * @param projectId 项目id
     * @param key 项目的key
     * @return User 用户对象
     **/
    @GetMapping("/user")
    public ResponseMessage getById(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.findById(userId, key, projectId));
    }

    /**
     * @method getLog 获取当前项目的所有日志
     * @param projectId 项目id
     * @param key 项目的key
     * @return Log 日志对象
     **/
    @GetMapping("/log")
    public ResponseMessage getLog(
            @RequestParam Long projectId,
            @RequestParam String key) {
        return ResponseMessage.successMessage(logService.findProjectLog(key, projectId));
    }

    /**
     * @method getProjectUser 获取当前项目所有用户信息
     * @param projectId 项目id
     * @param key 项目的key
     * @return UserVo 用户信息
     **/
    @GetMapping("/user/project")
    public ResponseMessage getProjectUser(
            @RequestParam Long projectId,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.findProjectUser(projectId, key));
    }

}