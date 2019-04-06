package com.dobe.redis.service.impl;

import com.dobe.redis.dao.UserRoleRedisInfoDao;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.UserRoleRedisInfo;
import com.dobe.redis.service.UserRoleRedisInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
@Service
public class UserRoleRedisInfoServiceImpl implements UserRoleRedisInfoService {

    @Autowired
    private UserRoleRedisInfoDao userRoleRedisInfoDao;
    
    @Override
    public ResponseEntity<?> addUserRoleRedisInfo(UserRoleRedisInfo userRoleRedisInfo) {
        return this.userRoleRedisInfoDao.addUserRoleRedisInfo(userRoleRedisInfo);
    }

    @Override
    public boolean delUserRoleRedisInfo(String name) {
        return this.userRoleRedisInfoDao.delUserRoleRedisInfo(name);
    }

    @Override
    public List<UserRoleRedisInfo> findUserRoleRedisInfoList(UserRoleRedisInfo userRoleRedisInfo) {
        return this.userRoleRedisInfoDao.findUserRoleRedisInfoList(userRoleRedisInfo);
    }
}
