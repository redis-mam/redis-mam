package com.dobe.redis.configuration;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author zc.ding
 * @since 2019/4/12
 */
@Component
public class ApplicationTaskListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        
        if(event.getApplicationContext().getParent() == null){
            
        }
    }
}
