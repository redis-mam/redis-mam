package com.dobe.redis.service.impl;

import com.dobe.redis.model.Pager;
import com.dobe.redis.model.RedisContainer;
import com.dobe.redis.service.ManagerService;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author zc.ding
 * @since 1.0
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    
    @Override
    public Pager findReidsItem(Pager pager, int dbIndex, String redisName, String key) {
        RedisTemplate<String, Object> redisTemplate = RedisContainer.OPS_MAP.get(redisName);
        Optional.ofNullable(((LettuceConnectionFactory)redisTemplate.getConnectionFactory())).ifPresent(f -> f.setDatabase(dbIndex));
        Set<String> set = redisTemplate.keys(key + "*");
        if (set != null && set.size() > 0) {
            pager.setRows(set.size());
            List<String> keys = new ArrayList<>(set);
            keys.sort(String::compareTo);
            List<String> list = keys.subList(pager.getStart(), pager.getEnd());
            pager.setData(redisTemplate.opsForValue().multiGet(list));
        }
        return pager;
    }
}
