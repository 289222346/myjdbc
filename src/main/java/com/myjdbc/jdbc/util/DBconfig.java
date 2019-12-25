package com.myjdbc.jdbc.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.myjdbc.core.constants.PropertiesJDBC;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.jdbc.constants.DBType;

import java.io.InputStream;
import java.util.Properties;


/**
 * @Author 陈文
 * @Date 2019/12/24  10:59
 * @Description 数据库配置文件
 */
public class DBconfig {

    /* 驱动 */
    protected String driver = DBType.MYSQL.getDriver();
    /* 数据库URL */
    protected String url = "jdbc:mysql://127.0.0.1:3306?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8";
    /* 用户 */
    protected String username = "root";
    /* 密码 */
    protected String password = "root";
    /* 连接池中最大连接数 */
    protected int maxActive = 0;
    /* 初始化时建立的物理连接的个数。初始化发生在显式调用init方法，或者第一次获取连接对象时 */
    protected int initialSize = 0;
    /* 获取连接时最长等待时间，单位是毫秒*/
    protected int maxWait = -1;
    /* Druid-阿里的数据库连接池 */
    protected DruidDataSource dataSource = new DruidDataSource();

//    public DBconfig(String driver, String url, String username, String password, int maxActive, int minActive, int closeTime) {
//        this.driver = driver;
//        this.url = url;
//        this.username = username;
//        this.password = password;
//        this.maxActive = maxActive;
//        this.minActive = minActive;
//        this.closeTime = closeTime;
//
//        //配置数据库连接池
//        dataSource.setDriverClassName(driver);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setMaxActive(maxActive);
//        dataSource.setInitialSize(minActive);
//        dataSource.setMinIdle(closeTime);
//    }

    public DBconfig() {
        Properties prop = new Properties();
        try {
            //读取属性文件a.properties
            InputStream in = this.getClass().getResourceAsStream("/dbconfig.properties");
            prop.load(in);     ///加载属性列表
//            Iterator<String> it = prop.stringPropertyNames().iterator();
            for (PropertiesJDBC propertiesType : PropertiesJDBC.values()) {
                String value = prop.getProperty(propertiesType.getCode());
                if (StringUtil.isEmpty(value)) {
                    continue;
                }
                if (PropertiesJDBC.DBTYPE.equals(propertiesType)) {
                    //配置数据库类别和SQL生成器
                    if (DBType.MYSQL.getCode().equals(value)) {
                        driver = DBType.MYSQL.getDriver();
                    } else if (DBType.ORACLE.getCode().equals(value)) {
                        driver = DBType.ORACLE.getDriver();
                    }
                } else if (PropertiesJDBC.URL.equals(propertiesType)) {
                    //获取数据库连接地址
                    url = value;
                } else if (PropertiesJDBC.USERNAME.equals(propertiesType)) {
                    //获取数据库连接地址
                    username = value;
                } else if (PropertiesJDBC.PASSWORD.equals(propertiesType)) {
                    password = value;
                } else if (PropertiesJDBC.MAX_ACTIVE.equals(propertiesType)) {
                    maxActive = Integer.parseInt(value);
                } else if (PropertiesJDBC.INITIAL_SIZE.equals(propertiesType)) {
                    initialSize = Integer.parseInt(value);
                } else if (PropertiesJDBC.MAX_WAIT.equals(propertiesType)) {
                    maxWait = Integer.parseInt(value);
                }
            }
            //配置数据库连接池
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setMaxActive(maxActive);
            dataSource.setInitialSize(initialSize);
            dataSource.setMinIdle(maxWait);
            in.close();
        } catch (Exception e) {
            System.out.println("错误：" + e);
        }
    }
}
