package com.dobe.redis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 默认配置
 * @author zc.ding
 * @since 1.0
 */
@Component
public class Config {

    @Value("${config.dir.path:}")
    private String configDirPath;
    @Value("${config.info.expire:900}")
    private Integer infoExpire;

    public String getConfigDirPath() {
        return configDirPath;
    }

    public void setConfigDirPath(String configDirPath) {
        this.configDirPath = configDirPath;
    }

    public Integer getInfoExpire() {
        return infoExpire;
    }

}
