package com.dobe.redis.service.impl;

import com.dobe.redis.dao.RedisInfoDao;
import com.dobe.redis.model.RedisContainer;
import com.dobe.redis.model.RedisInfo;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.StateEnum;
import com.dobe.redis.service.RedisInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author zc.ding
 * @since 1.0
 */
@Service
public class RedisServiceImpl implements RedisInfoService {
    
    private RedisInfoDao redisInfoDao;

    public RedisServiceImpl(RedisInfoDao redisInfoDao) {
        this.redisInfoDao = redisInfoDao;
    }
    
    @Override
    public ResponseEntity<?> addRedisInfo(RedisInfo redisInfo) {
        redisInfo.parseNodes();
        ResponseEntity<?> result = redisInfoDao.addRedisInfo(redisInfo);
        if (result.validSuc()) {
            RedisContainer.REDIS_ADD_QUEUE.add(redisInfo);
        }
        return result;
    }

    @Override
    public ResponseEntity<?> delRedisInfo(String name) {
        ResponseEntity<?> result = redisInfoDao.delRedisInfo(name);
        if(result.validSuc()){
            RedisContainer.REDIS_DEL_QUEUE.add((RedisInfo) result.getResMsg());
        }
        return result;
    }

    @Override
    public List<RedisInfo> findRedisInfoList(RedisInfo redisInfo) {
        return redisInfoDao.findRedisInfoList(redisInfo);
    }
}
