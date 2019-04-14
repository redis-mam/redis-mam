package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.RoleDao;
import com.dobe.redis.model.RedisContainer;
import org.springframework.stereotype.Component;

/**
 * @author zc.ding
 * @since 1.0
 */
@Component
public class RoleDaoImpl extends ConfigDaoImpl implements RoleDao {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.fileName = "roles.xml";
        super.module = "role";
        // 加载redis配置信息
        RedisContainer.ROLES.addAll(parseRole(getDocumentRoot().getChild("roles").getChildren("role")));
    }
}
