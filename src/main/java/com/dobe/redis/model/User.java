package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员信息
 *
 * @author zc.ding
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private String name = "";
    private String pwd = "";
    private String state = "1";
    private String description = "";
}



