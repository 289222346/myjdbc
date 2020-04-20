package com.myjdbc.core.constants;

/**
 * 数据库类型
 *
 * @Author: 陈文
 * @Date: 2020/4/20 12:24
 */
public enum DBType {

    ORACLE("oracle", "Oracle数据库", "oracle.jdbc.OracleDriver"),
    MYSQL("mysql", "MySql数据库", "com.mysql.jdbc.Driver"),
    MONGO("mongodb", "MongoDB数据库", null);

    /**
     * 配置文件中应该填写得类型名
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
    /**
     * 驱动
     */
    private String driver;

    DBType(String code, String remark, String driver) {
        this.code = code;
        this.remark = remark;
        this.driver = driver;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    public String getDriver() {
        return driver;
    }

    public static DBType getDBType(String code) {
        for (DBType dbType : DBType.values()) {
            if (dbType.getCode().equals(code)) {
                return dbType;
            }
        }
        return (DBType) PropertiesJDBC.DB_TYPE.getDefaultValue();
    }
}
