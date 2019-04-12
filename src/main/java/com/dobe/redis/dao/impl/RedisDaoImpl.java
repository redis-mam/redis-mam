package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.RedisInfoDao;
import com.dobe.redis.model.RedisInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
@Component
public class RedisDaoImpl extends ConfigDaoImpl implements RedisInfoDao {


    @Override
    public List<RedisInfo> findRedisInfoList() {
        return this.findRedisInfoList(new RedisInfo());
    }
}
