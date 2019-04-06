package com.dobe.redis.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 自定义MVC配置
 *
 * @author zc.ding
 * @since 1.0
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    /**
    *  关闭CORS
    *  @param registry
    *  @since                   ：2019/4/6
    *  @author                  ：zc.ding@foxmail.com
    */
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }


}
