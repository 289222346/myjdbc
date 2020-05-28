package com.myjdbc.core.config;

import com.myjdbc.core.config.properties.DbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 配置工具
 *
 * @author 陈文
 */
@Configuration
@EnableConfigurationProperties({DbConfig.class})
public class ConfigUtil {

    private static DbConfig dbConfig;

    @Autowired
    public ConfigUtil(DbConfig dbConfig) {
        ConfigUtil.dbConfig = dbConfig;
    }

    public static DbConfig getDbConfig() {
        return ConfigUtil.dbConfig;
    }

}
