package com.dobe.redis.controller;

import com.dobe.redis.model.Pager;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis操作
 *
 * @author zc.ding
 * @since 1.0
 */
@RestController
@RequestMapping("/manager/")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @RequestMapping("items")
    public Pager findItems(@RequestParam Pager pager, @RequestParam(defaultValue = "0") int dbIndex,
                           @RequestParam String redisName, @RequestParam String key) {
        return this.managerService.findReidsItem(pager, dbIndex, redisName, key);
    }

    /**
    *  删除redis数据
    *  @param dbIndex db索引
    *  @param redisName 配置名称
    *  @param key   多个ke中间以“,”分割
    *  @return com.dobe.redis.model.ResponseEntity<?>
    *  @since                   ：2019/4/15
    *  @author                  ：zc.ding@foxmail.com
    */
    @RequestMapping("del")
    public ResponseEntity<?> del(@RequestParam(defaultValue = "0") int dbIndex, @RequestParam String redisName,
                                 @RequestParam String key) {
        return this.managerService.del(dbIndex, redisName, key.split(","));
    }
}
