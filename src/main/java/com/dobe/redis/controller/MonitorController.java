package com.dobe.redis.controller;

import com.dobe.redis.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 查询监控信息
 *
 * @author zc.ding
 * @since 1.0
 */
@RestController
@RequestMapping("/monitor/")
public class MonitorController {

    private MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }
    
    /**
    *  查询监控信息
    *  @param name 节点名称
    *  @return java.util.Map<java.lang.String,java.util.List<java.util.Properties>>
    *  @author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("info")
    public Map<String, List<Properties>> info(String name) {
        return this.monitorService.findMonitorInfo(name);
    }
}
