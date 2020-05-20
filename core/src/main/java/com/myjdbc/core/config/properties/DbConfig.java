package com.myjdbc.core.config.properties;

import com.myjdbc.core.config.properties.enums.PropertiesFile;
import com.myjdbc.core.config.properties.enums.PropertiesJDBC;
import com.myjdbc.core.constants.DBType;

/**
 * @Author 陈文
 * @Date 2019/12/24  10:59
 * @Description 数据库配置文件
 */
public class DbConfig {

    /**
     * 数据库类型
     */
    public static final DBType DBTYPE;
    /**
     * 驱动
     */
    public static final String DRIVER;
    /**
     * 数据库URL
     */
    public static final String URL;
    /**
     * 用户名
     */
    public static final String USERNAME;
    /**
     * 用户密码
     */
    public static final String PASSWORD;
    /**
     * 连接池中最大连接数
     */
    public static final Integer MAX_ACTIVE;
    /**
     * 初始连接数
     * 初始化时建立的物理连接的个数。初始化发生在显式调用init方法，或者第一次获取连接对象时
     */
    public static final Integer INITIAL_SIZE;
    /**
     * 最长连接等待
     * 获取连接时最长等待时间，单位是毫秒
     */
    public static final Integer MAX_WAIT = 0;

    static {
        //属性工具
        PropertiesConfigUtil util = new PropertiesConfigUtil(PropertiesFile.JDBC);
        //获取数据库类型和驱动
        String dbTypeValue = util.readProperty(PropertiesJDBC.DB_TYPE.getCode()) + "";
        //获取数据库类型
        DBTYPE = DBType.getDBType(dbTypeValue);
        //获取驱动
        DRIVER = DBTYPE.getDriver();
        //获取数据库连接地址
        URL = util.readProperty(PropertiesJDBC.URL.getCode()) + "";
        //获取用户名
        USERNAME = util.readProperty(PropertiesJDBC.USERNAME.getCode()) + "";

        //获取用户密码
        PASSWORD = util.readProperty(PropertiesJDBC.PASSWORD.getCode()) + "";

        //获取最长连接
        MAX_ACTIVE = Integer.valueOf(util.readProperty(PropertiesJDBC.MAX_ACTIVE.getCode()) + "");

        //连接池初始大小
        INITIAL_SIZE = Integer.valueOf(util.readProperty(PropertiesJDBC.INITIAL_SIZE.getCode()) + "");
    }


}
