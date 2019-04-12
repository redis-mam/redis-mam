package com.dobe.redis.service;

import com.dobe.redis.model.ResponseEntity;

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
}
