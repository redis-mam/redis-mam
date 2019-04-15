package com.dobe.redis.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MonitorServiceTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    
    @Test
    public void testConfig(){
        System.out.println(redisConnectionFactory);
        System.out.println(redisConnectionFactory.getConnection().info());
        System.out.println("i'm ok");
    }

    @Test
    public void testSingleConnect(){

        RedisURI redisURI = RedisURI.builder().withHost("119.255.240.63").withPort(6380).withTimeout(Duration.ofSeconds(500)).build();
        RedisClient redisClient = RedisClient.create(redisURI);

        StatefulRedisConnection<String, String> statefulRedisConnection = redisClient.connect();
        System.out.println("Connected to Redis");
        System.out.println(statefulRedisConnection.sync().info("all"));
        statefulRedisConnection.close();
        redisClient.shutdown();
    }
    
    @Test
    public void testClusterConnect(){
        System.out.println("==================================");
        String ip = "119.255.240.63";
        int port = 7000;
        RedisURI cluster = RedisURI.builder().withTimeout(Duration.ofSeconds(5))
                .withHost(ip).withPort(port++)
                .withHost(ip).withPort(port++)
                .withHost(ip).withPort(port++)
                .withHost(ip).withPort(port++)
                .withHost(ip).withPort(port++)
                .withHost(ip).withPort(port)
                .build();
        RedisClusterClient clusterClient = RedisClusterClient.create(cluster);
        StatefulRedisClusterConnection<String, String> StatefulRedisClusterConnection= clusterClient.connect();
        System.out.println(StatefulRedisClusterConnection.sync().mget("name010", "name011"));
        List<String> keys = StatefulRedisClusterConnection.sync().keys("*");
        
        String info = StatefulRedisClusterConnection.sync().info();
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(info));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("系统：" + properties.getProperty("os"));
        StatefulRedisClusterConnection.close();
        clusterClient.shutdown();        
    }
    
    @Test
    public void testLettuceConnectionFactory(){
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration("119.255.240.63", 7000);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        System.out.println("okok");
        System.out.println(lettuceConnectionFactory.getConnection().keys("*".getBytes()).size());
        Optional.ofNullable(lettuceConnectionFactory.getConnection().info("all")).ifPresent(p -> {
            System.out.println("系统：" + p.getProperty("os"));
        });
    }

    @Test
    public void testHashCode() {
        String ip = "119.255.240.63";
        int port = 7000;
        System.out.println((ip + port).hashCode());
    }
    
}
