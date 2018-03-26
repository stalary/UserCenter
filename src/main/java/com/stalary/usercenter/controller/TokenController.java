
package com.stalary.usercenter.controller;

import com.stalary.usercenter.data.ResponseMessage;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.service.UserService;
import com.stalary.usercenter.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * TokenController
 *
 * @author lirongqian
 * @since 2018/03/24
 */
@Api(value = "使用token进行用户操作的controller", tags = "token用户操作接口")
@RequestMapping("/token")
@RestController
@Slf4j
public class TokenController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "注册", notes = "传入需要的user属性，进行注册，项目id必须要传")

    public ResponseMessage register(
            @RequestBody @ApiParam(name = "用户注册的对象", value = "仅用户名，密码，项目id为必填项") User user,
            HttpServletRequest request) {
        return ResponseMessage.successMessage(userService.tokenRegister(user, request));
    }

    /*@PostMapping("/login")
    @ApiOperation(value = "登陆", notes = "传入用户名和密码")
    public ResponseMessage login()*/
}