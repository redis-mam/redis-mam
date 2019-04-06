package com.dobe.redis.service;

import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.User;

import java.util.List;

public interface UserService {

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

    ResponseEntity<?> validUser(String name, String pwd);
}
