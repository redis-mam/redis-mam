package com.dobe.redis.model;

import com.dobe.redis.configuration.Config;
import com.dobe.redis.enums.StateEnum;
import com.dobe.redis.enums.TypeEnum;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
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
    private static final Map<String, StatefulRedisConnection<String, String>> REDIS_CLIENT_MONITOR_MAP = new ConcurrentHashMap<>();
    private static final Map<String, RedisClient> REDIS_CLIENT_MONITOR_RELEASE_MAP = new ConcurrentHashMap<>();
    /** 存储节点监控信息, Properties中添加key：uuid, value: (ip+port).hasCode()**/
    public static final Map<String, LinkedBlockingQueue<Properties>> MONITOR_MAP = new ConcurrentHashMap<>();
    /** 用于操作redis的缓存CURD **/
    public static final Map<String, Object> REDIS_CLIENT_OPS_MAP = new HashMap<>();
    public static final Map<String, Object> REDIS_CLIENT_OPS_RELEASE_MAP = new HashMap<>();
    public static final Map<String, Integer> REDIS_CLIENT_DB_MAP = new HashMap<>();
    /** 添加节点时待处理队列 **/
    public static final LinkedBlockingQueue<RedisInfo> REDIS_ADD_QUEUE = new LinkedBlockingQueue<>();
    /** 删除节点时待处理队列 **/
    public static final LinkedBlockingQueue<RedisInfo> REDIS_DEL_QUEUE = new LinkedBlockingQueue<>();
    
    public static final int DEFAULT_DB_INDEX = 0;

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
                    try {
                        RedisURI.Builder builder = RedisURI.builder().withHost(node.getHost()).withPort(node.getPort());
                        //设置密码
                        if (StringUtils.isNotBlank(node.getPwd())) {
                            builder = builder.withPassword(node.getPwd());
                        }
                        RedisClient redisClient = RedisClient.create(builder.build());
                        REDIS_CLIENT_MONITOR_MAP.put(node.getUuid(), redisClient.connect());
                        REDIS_CLIENT_MONITOR_RELEASE_MAP.put(node.getUuid(), redisClient);
                    } catch (Exception e) {
                        e.printStackTrace();
                        node.setState(StateEnum.STOP.getState());
                    }
                });
                // 初始化用于操作redis的连接
                initRedisClientOpsMap(redisInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
    *  初始化用户操作redis的客户端
    *  @param redisInfo redis信息
    *  @since                   ：2019/4/15
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void initRedisClientOpsMap(RedisInfo redisInfo) {
        final RedisURI.Builder builder = RedisURI.builder();
        switch (TypeEnum.parse(redisInfo.getType())) {
            case SENTIAL:
                throw new RuntimeException("暂未实现");
            case CLUSTER:
                redisInfo.getNodeList().forEach(node -> builder.withHost(node.getHost()).withPort(node.getPort()));
                if (StringUtils.isNotBlank(redisInfo.getPwd())) {
                    builder.withPassword(redisInfo.getPwd());
                }
                builder.withDatabase(DEFAULT_DB_INDEX);
                RedisClusterClient clusterClient = RedisClusterClient.create(builder.build());
                StatefulRedisClusterConnection<String, String> connect = clusterClient.connect();
                REDIS_CLIENT_OPS_MAP.put(redisInfo.getName(), connect);
                REDIS_CLIENT_OPS_RELEASE_MAP.put(redisInfo.getName(), clusterClient);
                break;
            default:
                Node node = redisInfo.getNodeList().get(0);
                builder.withHost(node.getHost()).withPort(node.getPort()).withDatabase(DEFAULT_DB_INDEX);
                //设置密码
                if (StringUtils.isNotBlank(node.getPwd())) {
                    builder.withPassword(node.getPwd());
                }
                RedisClient redisClient = RedisClient.create(builder.build());
                StatefulRedisConnection<String, String> connection = redisClient.connect();
                REDIS_CLIENT_OPS_MAP.put(redisInfo.getName(), connection);
                REDIS_CLIENT_OPS_RELEASE_MAP.put(redisInfo.getName(), redisClient);
                REDIS_CLIENT_DB_MAP.put(redisInfo.getName(), DEFAULT_DB_INDEX);
        }
    }
    

    /**
    *  节点删除后, 清空节点对应的所有缓存信息
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void redisDelQueueHandler() {
        while (state) {
            try {
                RedisInfo redisInfo = REDIS_DEL_QUEUE.take();
                LOG.info("需要删除的信息[{}]", redisInfo);
                //处理连接的每个节点
                redisInfo.getNodeList().forEach(node -> {
                    String key = node.getUuid();
                    //删除建立的连接
                    if(Objects.equals(node.getState(), StateEnum.RUNNING.getState())){
                        try {
                            REDIS_CLIENT_MONITOR_MAP.remove(key).close();
                            REDIS_CLIENT_MONITOR_RELEASE_MAP.remove(key).shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //删除监控数据
                    MONITOR_MAP.remove(key);
                });
                close(redisInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
    *  释放连接
    *  @param redisInfo redis配置信息
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void close(RedisInfo redisInfo) {
        try {
            ((StatefulConnection)(REDIS_CLIENT_OPS_MAP.remove(redisInfo.getName()))).close();
            Object obj = REDIS_CLIENT_OPS_RELEASE_MAP.remove(redisInfo.getName());
            if (obj instanceof RedisClusterClient) {
                ((RedisClusterClient) obj).shutdown();
            } else if (obj instanceof RedisClient) {
                ((RedisClient) obj).shutdown();
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
                REDIS_CLIENT_MONITOR_MAP.forEach((key, val) ->{
                    executorService.submit(() -> {
                        Properties p = new Properties();
                        String info = val.sync().info("ALL");
                        if (StringUtils.isNotBlank(info)) {
                            try {
                                p.load(new StringReader(info));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            p.setProperty("createTime", String.valueOf(System.currentTimeMillis()));
                            MONITOR_MAP.computeIfAbsent(key, v -> new LinkedBlockingQueue<>()).add(p);
                        }
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
