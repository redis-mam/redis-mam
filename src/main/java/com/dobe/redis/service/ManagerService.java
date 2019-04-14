package com.dobe.redis.service;

import com.dobe.redis.model.Pager;

public interface ManagerService {

    /**
    *  查询key对应value值
    *  @param pager 分页信息
    *  @param dbIndex 数据库 db0, db1......
    *  @param redisName redis配置名称
    *  @param key 关键字
    *  @return com.dobe.redis.model.Pager
    *  @author                  ：zc.ding@foxmail.com
    */
    Pager findReidsItem(Pager pager, int dbIndex, String redisName, String key);
}
