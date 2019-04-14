package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * redis 属性值
 * @author zc.ding
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    
    private String key;
    private String value;
    private String type;
    private long expire;
}
