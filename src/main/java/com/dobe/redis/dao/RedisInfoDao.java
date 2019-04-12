package com.dobe.redis.dao;

import com.dobe.redis.model.RedisInfo;
import com.dobe.redis.model.ResponseEntity;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
public interface RedisInfoDao {

    /**
     *  添加redis信息
     *  @param redisInfo redis连接信息
     *  @return boolean  状态
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> addRedisInfo(RedisInfo redisInfo);

    /**
     *  删除redis连接信息
     *  @param name redis唯一表示
     *  @return ResponseEntity<?> 状态
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    ResponseEntity<?> delRedisInfo(String name);

    /**
     *  查询redis连接配置信息
     *  @param redisInfo 检索条件
     *  @return java.util.List<com.dobe.redis.model.RedisInfo>
     *  @since                   ：2019/4/6
     *  @author                  ：zc.ding@foxmail.com
     */
    List<RedisInfo> findRedisInfoList(RedisInfo redisInfo);

    RedisInfo findRedisInfoList(String name);

    /**
    * 
    *  @return java.util.List<com.dobe.redis.model.RedisInfo>
    *  @since                   ：2019/4/12
    *  @author                  ：zc.ding@foxmail.com
    */
    List<RedisInfo> findRedisInfoList();
    
}
