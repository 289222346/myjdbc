package com.myjdbc.core.config.properties;

import com.myjdbc.core.constants.DBType;
import com.myjdbc.core.util.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author 陈文
 * @Date 2019/12/24  10:59
 * @Description 数据库配置文件
 */
@Component
@ConfigurationProperties(prefix = "dbconfig")
public class DbConfig {

    /**
     * 数据库类型
     */
    private DBType dbtype = DBType.MYSQL;

    /**
     * 驱动
     */
    private String driver;

    /**
     * 数据库URL
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 数据库名
     */
    private String databaseName;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 连接池中最大连接数
     */
    private Integer maxActive;

    /**
     * 初始连接数
     * 初始化时建立的物理连接的个数。初始化发生在显式调用init方法，或者第一次获取连接对象时
     */
    private static Integer initialSize;

    /**
     * 最长连接等待
     * 获取连接时最长等待时间，单位是毫秒
     */
    private static final Integer maxWait = 0;

    static {
//        //属性工具
//        PropertiesConfigUtil util = new PropertiesConfigUtil(PropertiesFile.JDBC);

//        //获取驱动
//        DRIVER = DBTYPE.getDriver();
//        //获取数据库连接地址
//        URL = util.readProperty(PropertiesJDBC.URL.getCode()) + "";
//        //获取用户名
//        USERNAME = util.readProperty(PropertiesJDBC.USERNAME.getCode()) + "";
//
//        //获取用户密码
//        PASSWORD = util.readProperty(PropertiesJDBC.PASSWORD.getCode()) + "";
//
//        //获取最长连接
//        MAX_ACTIVE = Integer.valueOf(util.readProperty(PropertiesJDBC.MAX_ACTIVE.getCode()) + "");
//
//        //连接池初始大小
//        INITIAL_SIZE = Integer.valueOf(util.readProperty(PropertiesJDBC.INITIAL_SIZE.getCode()) + "");
    }

    public DBType getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbTypeValue) {
        if (StringUtil.isNotEmpty(dbTypeValue)) {
            //获取数据库类型
            this.dbtype = DBType.getDBType(dbTypeValue);
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public static Integer getInitialSize() {
        return initialSize;
    }

    public static void setInitialSize(Integer initialSize) {
        DbConfig.initialSize = initialSize;
    }

    public static Integer getMaxWait() {
        return maxWait;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
