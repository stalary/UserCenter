
package com.stalary.usercenter.controller;

import com.stalary.usercenter.data.dto.ResponseMessage;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * TokenController
 * 使用token操作的Controller
 * @author lirongqian
 * @since 2018/03/24
 */
@RequestMapping("/token")
@RestController
public class TokenController {

    @Resource
    private UserService userService;

    /**
     * @method register 用户注册
     * @param user 用户对象
     * @param key 项目的key
     * @return token
     **/
    @PostMapping("/register")
    public ResponseMessage register(
            @RequestBody User user,
            @RequestParam String key,
            HttpServletRequest request) {
        return ResponseMessage.successMessage(userService.register(user, request, key));
    }

    /**
     * @method login 用户登陆，传入用户名和密码
     * @param user 用户对象
     * @param key  项目的key
     * @return token
     **/
    @PostMapping("/login")
    public ResponseMessage login(
            @RequestBody User user,
            @RequestParam String key,
            HttpServletRequest request) {
        return ResponseMessage.successMessage(userService.login(user, request, key));
    }

    /**
     * @method update 修改密码
     * @param user 用户对象
     * @param key 项目的key
     * @return token
     **/
    @PostMapping("/update/password")
    public ResponseMessage update(
            @RequestBody User user,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.update(user, key));
    }

    /**
     * @method updateInfo 更新用户信息
     * @param user 用户对象
     * @param key 项目的key
     * @return token
     **/
    @PostMapping("/update")
    public ResponseMessage updateInfo(
            @RequestBody User user,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.updateInfo(user, key));
    }

}