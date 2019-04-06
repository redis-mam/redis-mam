package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * redis的连接信息
 *
 * @author zc.ding
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisInfo {
    
    private String name = "";
    private String nodes = "";
    private String description = "";
}
