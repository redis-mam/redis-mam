package com.dobe.redis.service;

import com.dobe.redis.model.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author zc.ding
 * @since 2019/4/12
 */
public interface MonitorService {

    /**
    *  重新启动监听程序
    *  @return com.dobe.redis.model.ResponseEntity<?>
    *  @since                   ：2019/4/12
    *  @author                  ：zc.ding@foxmail.com
    */
    ResponseEntity<?> restartMonitor();

    /**
    *  查询监控信息
    *  @param name redis配置唯一标识
    *  @return java.util.Map<java.lang.String,java.util.List<java.util.Properties>>
    *  @author                  ：zc.ding@foxmail.com
    */
    Map<String, List<Properties>> findMonitorInfo(String name);
}
