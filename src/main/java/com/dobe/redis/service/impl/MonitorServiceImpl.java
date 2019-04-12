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
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@Service
public class MonitorServiceImpl implements MonitorService, InitializingBean {

    private final static String SECTION = "ALL";

    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    private RedisInfoDao redisInfoDao;

    public MonitorServiceImpl(RedisInfoDao redisInfoDao) {
        this.redisInfoDao = redisInfoDao;
    }


    @Override
    public ResponseEntity<?> restartMonitor() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public List<Properties> findMonitorInfo(String name) {
        List<Properties> list = new ArrayList<>();
        RedisInfo redisInfo = this.redisInfoDao.findRedisInfoList(name);
        redisInfo.getNodeList().forEach(r -> list.addAll(RedisContainer.MONITOR_QUEUE.stream()
                .filter(o -> Objects.equals(o.getProperty("uuid"), r.getUuid())).collect(Collectors.toList())));
        return list;
    }

    private void monitor() {
        for (String key : RedisContainer.REDIS_CONNECTS_MAP.keySet()) {
            executorService.submit(() -> {
                try{
                    RedisConnection redisConnection = RedisContainer.REDIS_CONNECTS_MAP.get(key).getConnection();
                    Optional.ofNullable(redisConnection.info(SECTION)).ifPresent(p -> {
                        p.setProperty("uuid", key);
//                        RedisContainer.addMonitorProperties(p);
                        RedisContainer.addMonitorProperties(key, p);
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
                // TODO: 更新节点的运行状态
            });
        }
    }

}
