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
        redisInfo.getNodeList().forEach(node -> {
            RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration(node.getHost(), node.getPort());
            //设置密码
            if (StringUtils.isNotBlank(node.getPwd())) {
                rsc.setPassword(node.getPwd());
            }
            try{
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(rsc);
                lettuceConnectionFactory.afterPropertiesSet();
                // 测试是否连接成功
                lettuceConnectionFactory.getConnection();
                RedisContainer.REDIS_CONNECTS_MAP.put(node.getUuid(), lettuceConnectionFactory);
            }catch(Exception e){
                e.printStackTrace();
                node.setState(StateEnum.STOP.getState());
            }
        });
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
