package com.myjdbc.jdbc.constants;


import com.myjdbc.jdbc.constants.DBType;

/**
 * @Author 陈文
 * @Date 2019/12/26  9:48
 * @Description JDBC配置
 */
public enum PropertiesJDBC {

    DBTYPE("myjdbc.dbType", "数据库类型", DBType.MYSQL),
    URL("myjdbc.url", "数据库连接地址", "jdbc:mysql://127.0.0.1:3306?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8"),
    USERNAME("myjdbc.username", "数据库登录名", "root"),
    PASSWORD("myjdbc.password", "数据库登录密码", "root"),
    MAX_ACTIVE("myjdbc.maxActive", "连接池中最大连接数", 20),
    INITIAL_SIZE("myjdbc.initialSize", "初始化时建立的物理连接的个数", 5),
    MAX_WAIT("myjdbc.maxWait", "获取连接时最长等待时间，单位是毫秒", 5000);


    private String code;
    private String remark;
    /* 默认值 */
    private Object defaultValue;


    PropertiesJDBC(String code, String remark, Object defaultValue) {
        this.code = code;
        this.remark = remark;
        this.defaultValue = defaultValue;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
