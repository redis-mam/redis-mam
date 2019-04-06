package com.dobe.redis.service;

import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.Role;

import java.util.List;

public interface RoleService {

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
}
