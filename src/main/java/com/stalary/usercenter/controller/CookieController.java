
package com.stalary.usercenter.controller;

import com.alibaba.fastjson.JSON;
import com.stalary.usercenter.data.dto.ResponseMessage;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.service.UserService;
import com.stalary.usercenter.utils.UCUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CookieController
 *
 * @author lirongqian
 * @since 2018/03/24
 */
@Api(value = "使用cookie进行用户操作的controller", tags = "cookie用户操作接口")
@RequestMapping("cookie")
@RestController
public class CookieController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "注册",notes = "传入用户注册对象和密钥")
    public ResponseMessage register(
            @RequestBody @ApiParam(name = "用户注册的对象", value = "仅用户名，密码，项目id为必填项") User user,
            @RequestParam String key,
            HttpServletRequest request,
            HttpServletResponse response) {
        User register = (User) userService.register(user, request, key, UCUtil.COOKIE);
        response.addCookie(new Cookie("cookie_user", JSON.toJSONString(register)));
        return ResponseMessage.successMessage(register);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登陆", notes = "传入用户登陆对象和密钥")
    public ResponseMessage login(
            @RequestBody @ApiParam(name = "用户登陆的对象", value = "传入用户名和密码和项目id，如果记住密码remember传true") User user,
            @RequestParam String key,
            HttpServletRequest request,
            HttpServletResponse response) {
        User login = (User) userService.login(user, request, key, UCUtil.COOKIE);
        response.addCookie(new Cookie("cookie_user", JSON.toJSONString(login)));
        return ResponseMessage.successMessage(login);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改密码", notes = "传入用户修改密码对象和密钥")
    public ResponseMessage update(
            @RequestBody @ApiParam(name = "用户修改密码的对象", value = "传入用户名和邮箱或者手机号以及新密码和项目id") User user,
            @RequestParam String key,
            HttpServletResponse response) {
        User update = (User) userService.update(user, key, UCUtil.COOKIE);
        response.addCookie(new Cookie("cookie_user", JSON.toJSONString(update)));
        return ResponseMessage.successMessage(update);
    }
}