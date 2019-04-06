package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * user role connection关系类
 *
 * @author zc.ding
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRedisInfo {
    
    private String name = "";
    private String userName = "";
    private String roleName = "";
    private String redisInfoName = "";
    private String description = "";
}
