package com.myjdbc.jdbc.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class DBconfig {

    public static final String DB_TYPE = "sqlGeneratorOracle";


    /* 驱动 */
    protected String driver = "oracle.jdbc.driver.OracleDriver";
    /* 数据库URL */
    protected String url = "jdbc:mysql://127.0.0.1:3306?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8";
    /* 用户 */
    protected String username = "root";
    /* 密码 */
    protected String password = "root";
    /* 最大连接数 */
    protected int maxCount = 50;
    /* 最小连接数 */
    protected int minCount = 10;
    /* 关闭空闲连接扫描时间（分钟） */
    protected int closeTime = 5;
    /* Druid-阿里的数据库连接池（分钟） */
    protected DruidDataSource dataSource = new DruidDataSource();

    public DBconfig(String driver, String url, String username, String password, int maxCount, int minCount, int closeTime) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxCount = maxCount;
        this.minCount = minCount;
        this.closeTime = closeTime;

        //配置数据库连接池
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxCount);
        dataSource.setInitialSize(minCount);
        dataSource.setMinIdle(closeTime);
    }

    public DBconfig() {
        Properties prop = new Properties();
        try {
            //读取属性文件a.properties
            InputStream in = this.getClass().getResourceAsStream("/dbconfig.properties");
            prop.load(in);     ///加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                switch (key) {
                    case "jdbc.url.jeecg":
                        url = prop.getProperty(key);
                        break;
                    case "jdbc.username.jeecg":
                        username = prop.getProperty(key);
                        break;
                    case "jdbc.password.jeecg":
                        password = prop.getProperty(key);
                        break;
                    case "maxcount":
                        maxCount = Integer.parseInt(prop.getProperty(key));
                        break;
                    case "mincount":
                        minCount = Integer.parseInt(prop.getProperty(key));
                        break;
                    case "closetime":
                        closeTime = Integer.parseInt(prop.getProperty(key));
                        break;
                }
            }
            //配置数据库连接池
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setMaxActive(maxCount);
            dataSource.setInitialSize(minCount);
            dataSource.setMinIdle(closeTime);
            in.close();
        } catch (Exception e) {
            System.out.println("错误：" + e);
        }
    }
}
