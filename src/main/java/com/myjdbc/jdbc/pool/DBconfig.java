package com.myjdbc.jdbc.pool;

import com.myjdbc.core.constants.PropertiesFile;
import com.myjdbc.core.constants.PropertiesJDBC;
import com.myjdbc.core.service.PropertiesUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.jdbc.constants.DBType;

/**
 * @Author 陈文
 * @Date 2019/12/24  10:59
 * @Description 数据库配置文件
 */
public class DBconfig {


    /* 数据库类型 */
    public static final DBType DBTYPE;
    /* 驱动 */
    public static final String DRIVER;
    /* 数据库URL */
    public static final String URL;
    /* 用户名 */
    public static final String USERNAME;
    /* 用户密码 */
    public static final String PASSWORD;
    /* 连接池中最大连接数 */
    public static final Integer MAX_ACTIVE;
    /* 初始化时建立的物理连接的个数。初始化发生在显式调用init方法，或者第一次获取连接对象时 */
    public static final Integer INITIAL_SIZE;
    /* 获取连接时最长等待时间，单位是毫秒*/
    public static final Integer MAX_WAIT = 0;

    static {
        //属性工具
        PropertiesUtil util = new PropertiesUtil(PropertiesFile.JDBC);

        //获取数据库类型和驱动
        String dbtypeValue = util.readProperty(PropertiesJDBC.DBTYPE.getCode());
        if (DBType.MYSQL.getCode().equals(dbtypeValue)) {
            DBTYPE = DBType.MYSQL;
        } else if (DBType.ORACLE.getCode().equals(dbtypeValue)) {
            DBTYPE = DBType.ORACLE;
        } else {
            DBTYPE = (DBType) PropertiesJDBC.DBTYPE.getDefaultValue();
        }
        DRIVER = DBTYPE.getDriver();

        //获取数据库连接地址
        String urlValue = util.readProperty(PropertiesJDBC.URL.getCode());
        if (StringUtil.isNotEmpty(urlValue)) {
            URL = urlValue;
        } else {
            URL = (String) PropertiesJDBC.URL.getDefaultValue();
        }

        //获取用户名
        String usernameValue = util.readProperty(PropertiesJDBC.USERNAME.getCode());
        if (StringUtil.isNotEmpty(urlValue)) {
            USERNAME = usernameValue;
        } else {
            USERNAME = (String) PropertiesJDBC.USERNAME.getDefaultValue();
        }

        //获取用户密码
        String passwordValue = util.readProperty(PropertiesJDBC.PASSWORD.getCode());
        if (StringUtil.isNotEmpty(urlValue)) {
            PASSWORD = passwordValue;
        } else {
            PASSWORD = (String) PropertiesJDBC.PASSWORD.getDefaultValue();
        }

        //获取最长连接
        String maxActiveValue = util.readProperty(PropertiesJDBC.MAX_ACTIVE.getCode());
        if (StringUtil.isNotEmpty(maxActiveValue)) {
            MAX_ACTIVE = Integer.valueOf(maxActiveValue);
        } else {
            MAX_ACTIVE = (Integer) PropertiesJDBC.MAX_ACTIVE.getDefaultValue();
        }

        String initialSizeValue = util.readProperty(PropertiesJDBC.INITIAL_SIZE.getCode());
        if (StringUtil.isNotEmpty(maxActiveValue)) {
            INITIAL_SIZE = Integer.valueOf(initialSizeValue);
        } else {
            INITIAL_SIZE = (Integer) PropertiesJDBC.INITIAL_SIZE.getDefaultValue();
        }

    }


}
