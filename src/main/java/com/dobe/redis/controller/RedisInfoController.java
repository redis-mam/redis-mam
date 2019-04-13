package com.dobe.redis.controller;

import com.dobe.redis.model.RedisInfo;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.service.RedisInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * redis 配置管理
 * @author zc.ding
 * @since 1.0
 */

@RestController
@RequestMapping("/redisInfo/")
public class RedisInfoController {
    
    private RedisInfoService redisInfoService;
    
    public RedisInfoController(RedisInfoService redisInfoService){
        this.redisInfoService = redisInfoService;
    }

    @RequestMapping("addRedisInfo")
    public ResponseEntity<?> addRedisInfo(RedisInfo redisInfo) {
        return this.redisInfoService.addRedisInfo(redisInfo);
    }

    @RequestMapping("delRedisInfo")
    public ResponseEntity<?> delRedisInfo(String name) {
        return this.redisInfoService.delRedisInfo(name);
    }

    @RequestMapping("redisInfoList")
    public List<RedisInfo> findRedisInfoList(RedisInfo redisInfo) {
        return this.redisInfoService.findRedisInfoList(redisInfo);
    }
    
}
