package com.dobe.redis.service.impl;

import com.dobe.redis.dao.RoleDao;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.Role;
import com.dobe.redis.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleDao roleDao;
    
    @Override
    public ResponseEntity<?> addRole(Role role) {
        return roleDao.addRole(role);
    }

    @Override
    public ResponseEntity<?> delRole(String name) {
        return roleDao.delRole(name);
    }

    @Override
    public List<Role> findRoleList(Role role) {
        return roleDao.findRoleList(role);
    }
}
