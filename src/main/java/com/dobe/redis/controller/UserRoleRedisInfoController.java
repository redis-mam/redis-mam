package com.dobe.redis.controller;

import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.UserRoleRedisInfo;
import com.dobe.redis.service.UserRoleRedisInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author zc.ding
 * @since 1.0
 */
@RestController
@RequestMapping("/userRoleRedisInfo/")
public class UserRoleRedisInfoController {

    private UserRoleRedisInfoService userRoleRedisInfoService;
    public UserRoleRedisInfoController(UserRoleRedisInfoService userRoleRedisInfoService) {
        this.userRoleRedisInfoService = userRoleRedisInfoService;
    }

    @RequestMapping("addUrr")
    public ResponseEntity<?> addUrr(UserRoleRedisInfo userRoleRedisInfo) {
        return this.userRoleRedisInfoService.addUserRoleRedisInfo(userRoleRedisInfo);
    }

    @RequestMapping("delUrr")    
    public boolean delUrr(String name) {
        return this.userRoleRedisInfoService.delUserRoleRedisInfo(name);
    }

    @RequestMapping("urrList")
    public List<UserRoleRedisInfo> urrList(UserRoleRedisInfo userRoleRedisInfo) {
        return this.userRoleRedisInfoService.findUserRoleRedisInfoList(userRoleRedisInfo);
    }
}
