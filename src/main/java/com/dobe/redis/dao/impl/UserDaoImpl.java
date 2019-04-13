package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.UserDao;
import com.dobe.redis.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import static com.dobe.redis.model.RedisContainer.USERS;

/**
 * @author zc.ding
 * @since 1.0
 */
@Component
public class UserDaoImpl extends ConfigDaoImpl implements UserDao, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载users
        USERS.addAll(parseUser(getDocumentRoot("users.xml").getChild("users").getChildren("user")));
    }
}
