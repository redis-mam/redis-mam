package com.dobe.redis.model;

import com.dobe.redis.configuration.Config;
import com.dobe.redis.enums.StateEnum;
import com.dobe.redis.enums.TypeEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisURI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * 数据容器
 * @author zc.ding
 * @since 1.0
 */
@Component
public class RedisContainer {

    private static final Logger LOG = LoggerFactory.getLogger(RedisContainer.class);
    
    private static final Integer SLEEP_TIME = 5000;
    /** 存储每个节点连接配置, key: (ip+port).hasCode() **/
    private static final Map<String, LettuceConnectionFactory> REDIS_CONNECTS_MAP = new ConcurrentHashMap<>();
    /** 存储节点监控信息, Properties中添加key：uuid, value: (ip+port).hasCode()**/
    public static final Map<String, LinkedBlockingQueue<Properties>> MONITOR_MAP = new ConcurrentHashMap<>();
    /** 用于操作redis的缓存CURD **/
    public static final Map<String, RedisTemplate> OPS_MAP = new HashMap<>();
    /** 添加节点时待处理队列 **/
    public static final LinkedBlockingQueue<RedisInfo> REDIS_ADD_QUEUE = new LinkedBlockingQueue<>();
    /** 删除节点时待处理队列 **/
    public static final LinkedBlockingQueue<RedisInfo> REDIS_DEL_QUEUE = new LinkedBlockingQueue<>();

    /** 用户信息缓存 **/
    public static final Vector<User> USERS = new Vector<>();
    /** 角色信息缓存 **/
    public static final Vector<Role> ROLES = new Vector<>();
    /** redis信息缓存 **/
    public static final Vector<RedisInfo> REDIS_INFOS = new Vector<>();
    /** 中间表缓存 **/
    public static final Vector<UserRoleRedisInfo> USER_ROLE_REDIS_INFOS = new Vector<>();

    /** 固定线程池 **/
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static boolean state = true;
    
    @Autowired
    private static Config config;
    

    /**
    *  添加节点后异步建立每个节点的连接信息
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void redisAddQueueHandler() {
        while (state) {
            try {
                RedisInfo redisInfo = REDIS_ADD_QUEUE.take();
                //处理连接的每个节点
                redisInfo.getNodeList().forEach(node -> {
                    RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration(node.getHost(), node.getPort());
                    //设置密码
                    if (StringUtils.isNotBlank(node.getPwd())) {
                        rsc.setPassword(node.getPwd());
                    }
                    try {
                        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(rsc);
                        lettuceConnectionFactory.afterPropertiesSet();
                        // 测试是否连接成功
                        lettuceConnectionFactory.getConnection();
                        RedisContainer.REDIS_CONNECTS_MAP.put(node.getUuid(), lettuceConnectionFactory);
                        // 初始化用于操作redis的连接
                        initRedisTemplate(redisInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        node.setState(StateEnum.STOP.getState());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    /**
    *  初始化用于操作redis的连接
    *  @param redisInfo 连接信息
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void initRedisTemplate(RedisInfo redisInfo) {
        LettuceConnectionFactory lettuceConnectionFactory;
        RedisConfiguration redisConfiguration;
        switch (TypeEnum.parse(redisInfo.getType())) {
            case SENTIAL:
                throw new RuntimeException("暂未实现");
            case CLUSTER:
                redisConfiguration = new RedisClusterConfiguration(Arrays.asList(redisInfo.getNodes().split(",")));
                break;
            default:
                Node node = redisInfo.getNodeList().get(0);
                redisConfiguration = new RedisStandaloneConfiguration(node.getHost(), node.getPort());
        }
        if (StringUtils.isNotBlank(redisInfo.getPwd())) {
            ((RedisConfiguration.WithPassword) redisConfiguration).setPassword(redisInfo.getPwd());
        }
        lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        RedisTemplate template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(lettuceConnectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // 值采用json序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        OPS_MAP.put(redisInfo.getName(), template);
    }

    /**
    *  节点删除后, 清空节点对应的所有缓存信息
    * 
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void redisDelQueueHandler() {
        while (state) {
            try {
                RedisInfo redisInfo = REDIS_ADD_QUEUE.take();
                //处理连接的每个节点
                redisInfo.getNodeList().forEach(node -> {
                    String key = node.getUuid();
                    //删除建立的连接
                    if(Objects.equals(node.getState(), StateEnum.RUNNING.getState())){
                        try {
                            LettuceConnectionFactory lettuceConnectionFactory = REDIS_CONNECTS_MAP.remove(key);
                            RedisConnection redisConnection = lettuceConnectionFactory.getConnection();
                            redisConnection.close();
                            redisConnection.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //删除监控数据
                    MONITOR_MAP.remove(key);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
    *  管理连接
    *  @param redisInfo
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void closeRedisConnection(RedisInfo redisInfo) {
        try {
            RedisTemplate redisTemplate = OPS_MAP.remove(redisInfo.getName());
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            if (connection != null) {
                connection.close();
                connection.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *  采集监控信息
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void collectionInfoHandler() {
        while (state) {
            try {
                REDIS_CONNECTS_MAP.forEach((key, val) ->{
                    executorService.submit(() -> {
                        RedisConnection redisConnection = val.getConnection();
                        Optional.ofNullable(redisConnection.info("ALL")).ifPresent(p -> {
                            p.setProperty("createTime", String.valueOf(System.currentTimeMillis()));
                            MONITOR_MAP.computeIfAbsent(key, v -> new LinkedBlockingQueue<>()).add(p);
                        });
                        // TODO: 更新节点的运行状态
                    });
                });
                Thread.sleep(SLEEP_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
    *  过期监控信息处理
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void expireInfoHandler() {
        while (state) {
            try {
                MONITOR_MAP.forEach((k, v) -> {
                    executorService.submit(() -> {
                        while (true) {
                            Properties properties = v.peek();
                            if (properties == null) {
                                break;
                            }
                            //删除过时监控信息
                            if (System.currentTimeMillis() - Long.parseLong(properties.getProperty(
                                    "createTime")) > config.getInfoExpire()) {
                                v.poll();
                            }else{
                                break;
                            }
                        }
                    });
                });
                Thread.sleep(SLEEP_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
