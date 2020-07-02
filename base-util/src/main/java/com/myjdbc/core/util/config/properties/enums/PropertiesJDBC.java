package com.myjdbc.core.util.config.properties.enums;


import com.myjdbc.core.util.config.constants.DBType;

/**
 * @Author 陈文
 * @Date 2019/12/26  9:48
 * @Description JDBC配置
 */
public enum PropertiesJDBC implements PropertiesEnum {

    /**
     * 数据库类型
     */
    DB_TYPE("myjdbc.dbType", "数据库类型", DBType.MYSQL),
    /**
     * 数据库连接地址
     */
    URL("myjdbc.url", "数据库连接地址", "jdbc:mysql://127.0.0.1:3306?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8"),
    /**
     * 数据库登录名
     */
    USERNAME("myjdbc.username", "数据库登录名", "root"),
    /**
     * 数据库登录密码
     */
    PASSWORD("myjdbc.password", "数据库登录密码", "root"),
    /**
     * 数据库
     */
    DATABASE("myjdbc.database", "数据库", null),
    /**
     * 公钥
     */
    PUBLIC_KEY("myjdbc.publicKey", "公钥", null),
    /**
     * 连接池中最大连接数
     */
    MAX_ACTIVE("myjdbc.maxActive", "连接池中最大连接数", 20),
    /**
     * 初始化时建立的物理连接的个数
     */
    INITIAL_SIZE("myjdbc.initialSize", "初始化时建立的物理连接的个数", 5),
    /**
     * 获取连接时最长等待时间，单位是毫秒
     */
    MAX_WAIT("myjdbc.maxWait", "获取连接时最长等待时间，单位是毫秒", 5000);


    /**
     * 配置属性名
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
    /**
     * 默认值
     */
    private Object defaultValue;


    PropertiesJDBC(String code, String remark, Object defaultValue) {
        this.code = code;
        this.remark = remark;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
