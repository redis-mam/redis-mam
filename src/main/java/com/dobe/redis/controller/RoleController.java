package com.dobe.redis.controller;

import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.Role;
import com.dobe.redis.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
@RestController
@RequestMapping("/role/")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping("addRole")
    public ResponseEntity<?> addRole(Role role) {
        return this.roleService.addRole(role);
    }

    @RequestMapping("delRole")
    public ResponseEntity<?> delRole(String name) {
        return this.roleService.delRole(name);
    }
    
    @RequestMapping("roleList")
    public List<Role> roleList(Role role) {
        return this.roleService.findRoleList(role);
    }
    
}
