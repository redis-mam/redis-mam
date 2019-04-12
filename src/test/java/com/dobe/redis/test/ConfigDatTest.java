package com.dobe.redis.test;

import com.dobe.redis.model.*;
import com.dobe.redis.dao.ConfigDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 *
 * @author zc.ding
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigDatTest {
    
    @Autowired
    private ConfigDao configDao;
    

    @Test
    public void testUser() {
        User user = new User("develop", "develop", "1", "");
        assertEquals(configDao.addUser(user).getResStatus(), ResponseEntity.SUCCESS_CODE);
        assertEquals(configDao.findUserList(new User("admin", "", "1", "")).size(), 1);
        assertEquals(configDao.updateUserState("admin", "1").getResStatus(), ResponseEntity.SUCCESS_CODE);

        configDao.findUserList(null).forEach(System.out::println);
    }

    @Test
    public void testRole() {
        Role role = new Role("system", "0", "0", "1", "", "", "系统管理员角色");
        assertEquals(configDao.addRole(role).getResStatus(), ResponseEntity.SUCCESS_CODE);
        Role cdt = new Role();
        cdt.setName("admin");
        assertEquals(configDao.findRoleList(cdt).size(), 1);

        configDao.findRoleList(null).forEach(System.out::println);
    }

    @Test
    public void testRedisInfo() {
        RedisInfo redisInfo = new RedisInfo("cloud", "127.0.0.1", "云配置", null, null, null);
        assertEquals(configDao.addRedisInfo(redisInfo).getResStatus(), ResponseEntity.SUCCESS_CODE);
        RedisInfo cdt = new RedisInfo();
        cdt.setName("clo");
        assertEquals(configDao.findRedisInfoList(cdt).size(), 1);

        configDao.findRedisInfoList(new RedisInfo()).forEach(System.out::println);
    }
    
    @Test
    public void testUserRoleRedisInfo() {
        UserRoleRedisInfo userRoleRedisInfo = new UserRoleRedisInfo("", "develop", "admin", "cloud", "测试");
        assertEquals(configDao.addUserRoleRedisInfo(userRoleRedisInfo).getResStatus(), ResponseEntity.SUCCESS_CODE);

        configDao.findUserRoleRedisInfoList(new UserRoleRedisInfo()).forEach(System.out::println);
    }
}
