package com.myjdbc.core.pool;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

/**
 * @Author: 陈文
 * @Date: 2020/4/18 11:48
 */
public class DataSourceFactory {

    /**
     * @Author 陈文
     * @Date 2019/12/26  11:44
     * @Description 暂时只支持德鲁伊连接池
     */
    public static DataSource creatDataSource(String poolType) {
        return creatDruidDataSource();
    }

    
    public static DataSource creatDruidDataSource() {
        //配置数据库连接池
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(DbConfig.DRIVER);
        dataSource.setUrl(DbConfig.URL);
        dataSource.setUsername(DbConfig.USERNAME);
        dataSource.setPassword(DbConfig.PASSWORD);
        if (DbConfig.MAX_ACTIVE != null) {
            dataSource.setMaxActive(DbConfig.MAX_ACTIVE);
        }
        if (DbConfig.INITIAL_SIZE != null) {
            dataSource.setInitialSize(DbConfig.INITIAL_SIZE);
        }
        if (DbConfig.MAX_WAIT != null) {
            dataSource.setMinIdle(DbConfig.MAX_WAIT);
        }
        return dataSource;
    }
}
