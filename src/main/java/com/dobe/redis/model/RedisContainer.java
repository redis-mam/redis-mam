package com.dobe.redis.model;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * 变量容器
 * @author zc.ding
 * @since 1.0
 */
public class RedisContainer {
    
    /** 存储每个节点连接配置, key: (ip+port).hasCode() **/
    public static final Map<String, LettuceConnectionFactory> REDIS_CONNECTS_MAP = new ConcurrentHashMap<>();
    /** 存储节点监控信息, Properties中添加key：uuid, value: (ip+port).hasCode()**/
    public static final Map<String, ArrayList<Properties>> MONITOR_MAP = new ConcurrentHashMap<>();
    public static final LinkedBlockingQueue<Properties> MONITOR_QUEUE = new LinkedBlockingQueue<>();

    public static final Vector<User> USERS = new Vector<>();
    public static final Vector<Role> ROLES = new Vector<>();
    public static final Vector<RedisInfo> REDIS_INFOS = new Vector<>();
    public static final Vector<UserRoleRedisInfo> USER_ROLE_REDIS_INFOS = new Vector<>();

    /**
    *  向MONITOR_QUEUE添加监控信息，捕获异常
    *  @param properties    监控信息
    *  @since                   ：2019/4/13
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void addMonitorProperties(Properties properties) {
        try {
            MONITOR_QUEUE.put(properties);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
    *  TODO
    *  @param key   key: (ip+port).hasCode()
    *  @param properties    监控信息
    *  @since                   ：2019/4/13
    *  @author                  ：zc.ding@foxmail.com
    */
    public static synchronized void addMonitorProperties(String key, Properties properties) {
        MONITOR_MAP.computeIfAbsent(key, v -> new ArrayList<>()).add(properties);
    }
}
