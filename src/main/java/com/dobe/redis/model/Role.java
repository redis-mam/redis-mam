package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色信息
 * @author zc.ding
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    private String name = "";
    private String create = "0";
    private String delete = "0";
    private String read = "1";
    private String includeKey = "";
    private String exclusiveKey = "";
    private String description = "";
}
