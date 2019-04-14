package com.dobe.redis.model;

import com.dobe.redis.configuration.Config;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

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
    public static final Map<String, Object> OPS_MAP = new HashMap<>();
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

//    /**
//    *  存储监控信息
//    *  @param key   key: (ip+port).hasCode()
//    *  @param properties    监控信息
//    *  @since                   ：2019/4/13
//    *  @author                  ：zc.ding@foxmail.com
//    */
//    private static synchronized void addMonitorProperties(String key, Properties properties) {
//        MONITOR_MAP.computeIfAbsent(key, v -> new ArrayList<>()).add(properties);
//    }

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
