package com.dobe.redis.service.impl;

import com.dobe.redis.dao.RedisInfoDao;
import com.dobe.redis.model.RedisInfo;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.service.RedisInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
@Service
public class RedisServiceImpl implements RedisInfoService {
    
    @Autowired
    private RedisInfoDao redisInfoDao;
    
    @Override
    public ResponseEntity<?> addRedisInfo(RedisInfo redisInfo) {
        redisInfo.parseNodes();
        return redisInfoDao.addRedisInfo(redisInfo);
    }

    @Override
    public ResponseEntity<?> delRedisInfo(String name) {
        return redisInfoDao.delRedisInfo(name);
    }

    @Override
    public List<RedisInfo> findRedisInfoList(RedisInfo redisInfo) {
        return redisInfoDao.findRedisInfoList(redisInfo);
    }
}
