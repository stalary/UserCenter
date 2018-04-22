
package com.stalary.usercenter.controller;

import com.stalary.usercenter.data.dto.ResponseMessage;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.service.UserService;
import com.stalary.usercenter.utils.UCUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * TokenController
 *
 * @author lirongqian
 * @since 2018/03/24
 */
@Api(value = "使用token进行用户操作的controller", tags = "token用户操作接口")
@RequestMapping("/token")
@RestController
public class TokenController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "注册",notes = "传入用户注册对象和密钥")
    public ResponseMessage register(
            @RequestBody @ApiParam(name = "用户注册的对象", value = "仅用户名，密码，项目id为必填项") User user,
            @RequestParam String key,
            HttpServletRequest request) {
        return ResponseMessage.successMessage(userService.register(user, request, key));
    }

    @PostMapping("/login")
    @ApiOperation(value = "登陆", notes = "传入用户登陆对象和密钥")
    public ResponseMessage login(
            @RequestBody @ApiParam(name = "用户登陆的对象", value = "传入用户名和密码和项目id，如果记住密码remember传true") User user,
            @RequestParam String key,
            HttpServletRequest request) {
        return ResponseMessage.successMessage(userService.login(user, request, key));
    }

    @PostMapping("/update/password")
    @ApiOperation(value = "修改密码", notes = "传入用户修改密码对象和密钥")
    public ResponseMessage update(
            @RequestBody @ApiParam(name = "用户修改密码的对象", value = "传入用户名和邮箱或者手机号以及新密码和项目id") User user,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.update(user, key));
    }

    @PostMapping("update")
    @ApiOperation(value = "修改信息", notes = "传入用户对象，进行修改信息")
    public ResponseMessage updateInfo(
            @RequestBody User user,
            @RequestParam String key) {
        return ResponseMessage.successMessage(userService.updateInfo(user, key));
    }

}