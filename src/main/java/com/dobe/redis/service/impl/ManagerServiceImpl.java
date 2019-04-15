package com.dobe.redis.service.impl;

import com.dobe.redis.enums.TypeEnum;
import com.dobe.redis.model.*;
import com.dobe.redis.service.ManagerService;
import com.dobe.redis.service.RedisInfoService;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author zc.ding
 * @since 1.0
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    
    @Autowired
    private RedisInfoService redisInfoService;
    
    @Override
    @SuppressWarnings("unchecked")
    public Pager findReidsItem(Pager pager, int dbIndex, String redisName, String key) {
        RedisInfo redisInfo = this.redisInfoService.findRedisInfoByName(redisName);
        Object obj = RedisContainer.REDIS_CLIENT_OPS_MAP.get(redisName);
        key = key + "*";
        if (Objects.equals(redisInfo.getType(), TypeEnum.CLUSTER.getName())) {
            StatefulRedisClusterConnection<String, String> client = (StatefulRedisClusterConnection<String, String>)obj;
            String[] keys = getKeys(pager, key, k->client.sync().keys(k));
            pager.setData(fitItem(client.sync().mget(keys), k -> client.sync().ttl(k)));
        }else if(Objects.equals(redisInfo.getType(), TypeEnum.SINGLE.getName())){
            StatefulRedisConnection<String, String> client = (StatefulRedisConnection<String, String>)obj;
            String[] keys = getKeys(pager, key, k->client.sync().keys(k));
            //切换是用的db，CLUSTER模式不需要切换，都是db0
            client.sync().swapdb(RedisContainer.REDIS_CLIENT_DB_MAP.get(redisName), dbIndex);
            pager.setData(fitItem(client.sync().mget(keys), k -> client.sync().ttl(k)));
        }
        return pager;
    }

    /**
    *  模糊查询所有的key
    *  @param pager 分页信息
    *  @param key   关键字
    *  @param function  函数
    *  @return java.lang.String[]
    *  @since                   ：2019/4/15
    *  @author                  ：zc.ding@foxmail.com
    */
    private String[] getKeys(Pager pager, String key, Function<String, List<String>> function) {
        List<String> keys = function.apply(key);
        pager.setRows(keys.size());
        //排序
//        keys.sort(String::compareTo);
        keys = keys.subList(pager.getStart(), pager.getEnd());
        return keys.toArray(new String[]{});
    }

    /**
    *  封装返回信息
    *  @param list key value值
    *  @param function  函数
    *  @return java.util.List<com.dobe.redis.model.Item>
    *  @since                   ：2019/4/15
    *  @author                  ：zc.ding@foxmail.com
    */
    private List<Item> fitItem(List<KeyValue<String, String>> list, Function<String, Long> function) {
        List<Item> result = new ArrayList<>();
        list.forEach(o -> {
            Item item = new Item();
            item.setKey(o.getKey());
            item.setValue(o.getValue());
            item.setExpire(function.apply(o.getKey()));
            result.add(item);
        });
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> del(int dbIndex, String redisName, String[] keys) {
        long num = 0;
        RedisInfo redisInfo = this.redisInfoService.findRedisInfoByName(redisName);
        Object obj = RedisContainer.REDIS_CLIENT_OPS_MAP.get(redisName);
        if (Objects.equals(redisInfo.getType(), TypeEnum.CLUSTER.getName())) {
            num = ((StatefulRedisClusterConnection<String, String>)obj).sync().del(keys);
        }else if(Objects.equals(redisInfo.getType(), TypeEnum.SINGLE.getName())){
            num = ((StatefulRedisConnection<String, String>)obj).sync().del(keys);
        }
        return ResponseEntity.success("需要删除" + keys.length + "个key，已删除" + num + "个");
    }
}
