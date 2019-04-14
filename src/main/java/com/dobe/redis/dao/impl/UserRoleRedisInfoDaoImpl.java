package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.UserRoleRedisInfoDao;
import com.dobe.redis.model.RedisContainer;
import org.springframework.stereotype.Component;

/**
 * @author zc.ding
 * @since 1.0
 */
@Component
public class UserRoleRedisInfoDaoImpl extends ConfigDaoImpl implements UserRoleRedisInfoDao {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        super.fileName = "user_role_redis.xml";
        super.module = "urr";
        // 加载redis配置信息
        RedisContainer.USER_ROLE_REDIS_INFOS.addAll(parseUserRoleRedisInfo(getDocumentRoot()
                .getChild("userRoleRedisInfos").getChildren("userRoleRedisInfo")));
    }
}
