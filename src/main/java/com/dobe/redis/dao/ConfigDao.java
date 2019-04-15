package com.dobe.redis.dao;

import com.dobe.redis.model.*;

import java.util.List;

public interface ConfigDao {

    /**
     *  添加用户
     *  @param user  用户信息
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> addUser(User user);

    /**
     *  删除用户信息
     *  @param name  用户唯一标识
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> delUser(String name);

    /**
     *  更新用户信息
     *  @param name  用户标识
     *  @param state 状态
     *  @return boolean 状态
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> updateUserState(String name, String state);

    /**
     *  查询所有用户信息
     *  @param user  查询条件
     *  @return java.util.List<com.dobe.redis.model.User>
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    List<User> findUserList(User user);

    User findUserByName(String name);
    
    /**
    *  添加角色
    *  @param role  角色信息
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> addRole(Role role);

    /**
    *  删除角色
    *  @param name  角色信息
    *  @return ResponseEntity<?>  状态
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> delRole(String name);

    /**
    *  查询所有角色信息
    *  @param role  查询条件
    *  @return java.util.List<com.dobe.redis.model.Role>
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    List<Role> findRoleList(Role role);

    /**
    *  添加redis信息
    *  @param redisInfo redis连接信息
    *  @return boolean  状态
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> addRedisInfo(RedisInfo redisInfo);

    /**
    *  删除redis连接信息
    *  @param name redis唯一表示
    *  @return ResponseEntity<?> 状态
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> delRedisInfo(String name);
    
    /**
    *  查询redis连接配置信息
    *  @param redisInfo 检索条件
    *  @return java.util.List<com.dobe.redis.model.RedisInfo>
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    List<RedisInfo> findRedisInfoList(RedisInfo redisInfo);

    RedisInfo findRedisInfoByName(String name);

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
