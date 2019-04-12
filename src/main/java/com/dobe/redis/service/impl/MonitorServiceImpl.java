package com.dobe.redis.service.impl;

import com.dobe.redis.dao.ConfigDao;
import com.dobe.redis.dao.RedisInfoDao;
import com.dobe.redis.model.Node;
import com.dobe.redis.model.RedisInfo;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.service.MonitorService;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@Service
public class MonitorServiceImpl implements MonitorService, InitializingBean {


    Map<String, Object> redisConnectsMap = new HashMap<>();
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
    
    public void monitor(){
        
        
    }
    
    static class Monitor{
        private int time = 1000;
        private LinkedBlockingQueue<Node> linkedBlockingQueue = new LinkedBlockingQueue<>(6);
        List<Node> node = new ArrayList<>();
        
        public void addNode(Node node) {
            try{
                linkedBlockingQueue.put(node);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public void monitor(){
            node.forEach(node -> {
                
            });
        }
        
    }
}
