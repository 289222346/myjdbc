package com.myjdbc.jdbc.pool;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

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
        dataSource.setDriverClassName(DBconfig.DRIVER);
        dataSource.setUrl(DBconfig.URL);
        dataSource.setUsername(DBconfig.USERNAME);
        dataSource.setPassword(DBconfig.PASSWORD);
        if (DBconfig.MAX_ACTIVE != null) {
            dataSource.setMaxActive(DBconfig.MAX_ACTIVE);
        }
        if (DBconfig.INITIAL_SIZE != null) {
            dataSource.setInitialSize(DBconfig.INITIAL_SIZE);
        }
        if (DBconfig.MAX_WAIT != null) {
            dataSource.setMinIdle(DBconfig.MAX_WAIT);
        }
        return dataSource;
    }
}
