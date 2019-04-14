package com.dobe.redis.controller;

import com.dobe.redis.model.Pager;
import com.dobe.redis.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Pager findItems(Pager pager, int dbIndex, String redisName, String key) {
        return this.managerService.findReidsItem(pager, dbIndex, redisName, key);
    }
}
