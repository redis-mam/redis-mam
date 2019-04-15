package com.dobe.redis.model;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis 模板方法
 *
 * @author zc.ding
 * @since 2019/4/15
 */
public class RedisTemplate {

    private static Map<String, StatefulRedisConnection<String, String>> REDIS_CLIENT_MAP = new ConcurrentHashMap<>();
    private static Map<String, RedisClient> REDIS_CLIENT_RELEASE_MAP = new ConcurrentHashMap<>();
    
    public static StatefulRedisConnection<String, String> getRedisClient(RedisInfo redisInfo, int dbIndex) {
        final RedisURI.Builder builder = RedisURI.builder();
        Node node = redisInfo.getNodeList().get(0);
        builder.withHost(node.getHost()).withPort(node.getPort()).withDatabase(dbIndex);
        //设置密码
        if (StringUtils.isNotBlank(node.getPwd())) {
            builder.withPassword(node.getPwd());
        }
        RedisClient redisClient = RedisClient.create(builder.build());
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        REDIS_CLIENT_MAP.put(redisInfo.getName(), connection);
        REDIS_CLIENT_RELEASE_MAP.put(redisInfo.getName(), redisClient);
        return connection;
    }
    
    public static void close(String name) {
        REDIS_CLIENT_MAP.remove(name).close();
        REDIS_CLIENT_RELEASE_MAP.remove(name).shutdown();
    }


    public static Pager loadData(Pager pager, int dbIndex, RedisInfo redisInfo, String key) {
//        StatefulRedisConnection<String, String> client = getRedisClient(redisInfo, dbIndex);
//        List<String> keys = client.sync().keys(key + "*");
//        pager.setRows(keys.size());
//        keys.sort(String::compareTo);
//        keys = keys.subList(pager.getStart(), pager.getEnd());
//        List<KeyValue<String, String>> list = client.sync().mget(keys.toArray(new String[]{}));
//        client.sync().swapdb(1, 2);
//        List<Item> result = new ArrayList<>();
//        list.forEach(o -> {
//            Item item = new Item();
//            item.setKey(o.getKey());
//            item.setValue(o.getValue());
//            item.setExpire(client.sync().ttl(o.getKey()));
//            result.add(item);
//        });
//        pager.setData(result);
        return pager;
    }
    
}
