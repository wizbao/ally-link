package com.abao.allylink.controller;

import com.abao.allylink.common.BaseResponse;
import com.abao.allylink.common.ErrorCode;
import com.abao.allylink.common.ResultUtils;
import com.abao.allylink.exception.BusinessException;
import com.abao.allylink.model.entity.Team;
import com.abao.allylink.model.entity.User;
import com.abao.allylink.model.request.TeamAddRequest;
import com.abao.allylink.service.TeamService;
import com.abao.allylink.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description
 * @Author zhangweibao
 * @Date 2024/7/2 21:52
 * @Version 1.0
 **/
@RestController
@RequestMapping("/team")
@Api(tags = "队伍 API实现")
public class TeamController {

    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    @ApiOperation(value = "添加队伍")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

}