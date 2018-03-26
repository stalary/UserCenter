/**
 * @(#)FacadeController.java, 2018-03-27.
 * <p>
 * Copyright 2018 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stalary.usercenter.controller;

import com.stalary.usercenter.data.ResponseMessage;
import com.stalary.usercenter.data.entity.User;
import com.stalary.usercenter.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * FacadeController
 *
 * @author lirongqian
 * @since 2018/03/27
 */
@RequestMapping("/facade")
@Api(value = "对外提供的controller", tags = "提供一些基本的信息")
@RestController
public class FacadeController {

    @Resource
    private ProjectService projectService;

    @PostMapping("/project")
    @ApiOperation(value = "注册", notes = "传入项目名")
    public ResponseMessage register(
            @RequestParam String name) {
        return ResponseMessage.successMessage(projectService.save(name));
    }

    @GetMapping("/project")
    @ApiOperation(value = "获得项目信息", notes = "传入项目名")
    public ResponseMessage getInfo(
            @RequestParam String name) {
        return ResponseMessage.successMessage(projectService.get(name));
    }

}