package com.dobe.redis.configuration;

import com.dobe.redis.model.RedisContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成触发事件
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@Component
public class HandlerTasks implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RedisContainer.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        
        if(event.getApplicationContext().getParent() == null){
            // 启动添加redis配置监听器
            LOG.info("启动添加redis配置监听器");
            new Thread(RedisContainer::redisAddQueueHandler, "add_redis_info_handler_thread").start();
            // 启动删除redis配置监听器
            LOG.info("启动删除redis配置监听器");
            new Thread(RedisContainer::redisDelQueueHandler, "del_redis_info_handler_thread").start();
            // 启动采集redis监控信息线程
            LOG.info("启动采集redis监控信息线程");
            new Thread(RedisContainer::collectionInfoHandler, "collection_info_handler_thread").start();
            // 启动清理过期redis监控消息的线程
            LOG.info("启动清理过期redis监控消息的线程");
            new Thread(RedisContainer::expireInfoHandler, "expire_info_handler_thread").start();
        }
    }
}
