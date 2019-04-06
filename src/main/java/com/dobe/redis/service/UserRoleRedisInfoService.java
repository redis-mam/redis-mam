package com.dobe.redis.service;

import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.UserRoleRedisInfo;

import java.util.List;

public interface UserRoleRedisInfoService {

    /**
     *  添加user role redisInfo关联信息
     *  @param userRoleRedisInfo user role redisInfo关联信息
     *  @return ResponseEntity<?>
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> addUserRoleRedisInfo(UserRoleRedisInfo userRoleRedisInfo);

    /**
     *  删除user role redisInfo关联信息
     *  @param name 唯一标识
     *  @return boolean
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    boolean delUserRoleRedisInfo(String name);

    /**
     *  查询user role redisInfo关联信息
     *  @param userRoleRedisInfo 检索条件
     *  @return java.util.List<com.dobe.redis.model.UserRoleRedisInfo>
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    List<UserRoleRedisInfo> findUserRoleRedisInfoList(UserRoleRedisInfo userRoleRedisInfo);
}
