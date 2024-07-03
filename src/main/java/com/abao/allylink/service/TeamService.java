package com.abao.allylink.service;

import com.abao.allylink.model.entity.Team;
import com.abao.allylink.model.entity.User;
import com.abao.allylink.model.request.TeamJoinRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 韦小宝
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-07-02 21:43:17
*/
public interface TeamService extends IService<Team> {

    /**
     * 添加队伍
     *
     * @param team 队伍
     * @param loginUser 登录用户
     * @return 队伍id
     */
    long addTeam(Team team, User loginUser);

    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);
}
