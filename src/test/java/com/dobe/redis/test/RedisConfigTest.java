package com.dobe.redis.test;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.resource.ClientResources;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

/**
 * TODO
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisConfigTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Test
    public void testConfig(){
        System.out.println(redisConnectionFactory);
        System.out.println(redisConnectionFactory.getConnection().info());
        System.out.println("i'm ok");
    }
    
    
    @Test
    public void test(){
        
        RedisURI redisURI = RedisURI.builder().withHost("119.255.240.63").withPort(7000).withTimeout(Duration.ofSeconds(5)).build();
        RedisClient redisClient = RedisClient.create(redisURI);
        
        StatefulRedisConnection<String, String> statefulRedisConnection = redisClient.connect();
        System.out.println("Connected to Redis");
        System.out.println(statefulRedisConnection.sync().info());

        RedisClusterClient clusterClient = RedisClusterClient.create(redisURI);
        StatefulRedisClusterConnection<String, String> StatefulRedisClusterConnection= clusterClient.connect();
        System.out.println(StatefulRedisClusterConnection.sync().info());


        statefulRedisConnection.close();
        redisClient.shutdown();
        StatefulRedisClusterConnection.close();
        clusterClient.shutdown();
    
    }
    
}
