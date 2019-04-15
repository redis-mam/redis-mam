package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.UserDao;
import org.springframework.stereotype.Component;

import static com.dobe.redis.model.RedisContainer.USERS;

/**
 * @author zc.ding
 * @since 1.0
 */
@Component
public class UserDaoImpl extends ConfigDaoImpl implements UserDao {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.fileName = "users.xml";
        super.module = "user";
        // 加载users
        USERS.addAll(parseUser(getDocumentRoot().getChild("users").getChildren("user")));
    }
}
