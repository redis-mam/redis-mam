package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.RedisInfoDao;
import com.dobe.redis.model.RedisContainer;
import com.dobe.redis.model.RedisInfo;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dobe.redis.model.RedisContainer.REDIS_INFOS;
import static com.dobe.redis.model.RedisContainer.USERS;

/**
 * @author zc.ding
 * @since 1.0
 */
@Component
public class RedisDaoImpl extends ConfigDaoImpl implements RedisInfoDao {


    @Override
    public RedisInfo findRedisInfoList(String name) {
        RedisInfo redisInfo = new RedisInfo();
        redisInfo.setName(name);
        return this.findRedisInfoList(redisInfo).get(0);
    }

    @Override
    public List<RedisInfo> findRedisInfoList() {
        return this.findRedisInfoList(new RedisInfo());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载redis配置信息
        RedisContainer.REDIS_INFOS.addAll(parseRedisInfo(getDocumentRoot("redis_infos.xml")
                .getChild("redisInfos").getChildren("redisInfo")));
    }
}
