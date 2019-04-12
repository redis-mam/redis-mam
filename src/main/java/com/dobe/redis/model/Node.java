package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * redis单节点信息
 * @author zc.ding
 * @since 2019/4/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    
    private String host;
    private int port;
    private String pwd;
    private String state;

    public String getUuid() {
        return String.valueOf((this.host + this.port).hashCode());
    }
}
