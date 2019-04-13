package com.dobe.redis.service.impl;

import com.dobe.redis.dao.RedisInfoDao;
import com.dobe.redis.model.RedisContainer;
import com.dobe.redis.model.RedisInfo;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.service.MonitorService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监控管理
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    private RedisInfoDao redisInfoDao;

    public MonitorServiceImpl(RedisInfoDao redisInfoDao) {
        this.redisInfoDao = redisInfoDao;
    }


    @Override
    public ResponseEntity<?> restartMonitor() {
        return null;
    }

    @Override
    public Map<String, List<Properties>> findMonitorInfo(String name) {
        Map<String, List<Properties>> result = new HashMap<>();
        RedisInfo redisInfo = this.redisInfoDao.findRedisInfoList(name);
        redisInfo.getNodeList().forEach(r -> result.put(r.getUuid(), RedisContainer.MONITOR_MAP.get(r.getUuid())));
        return result;
    }


}
