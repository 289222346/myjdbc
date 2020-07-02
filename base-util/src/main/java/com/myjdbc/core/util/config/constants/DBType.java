package com.myjdbc.core.util.config.constants;

import com.myjdbc.core.util.config.properties.enums.PropertiesJDBC;

/**
 * 数据库类型
 *
 * @Author: 陈文
 * @Date: 2020/4/20 12:24
 */
@SuppressWarnings({"ALL", "AlibabaEnumConstantsMustHaveComment"})
public enum DBType {

    /**
     * Oracle数据库
     */
    ORACLE("oracle", "Oracle数据库", "oracle.jdbc.OracleDriver"),
    /**
     * MySql数据库
     */
    MYSQL("mysql", "MySql数据库", "com.mysql.jdbc.Driver"),
    /**
     * MongoDB数据库
     */
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
