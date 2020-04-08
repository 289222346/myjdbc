package com.myjdbc.jdbc.constants;

import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import com.myjdbc.jdbc.core.sqlgenerator.impl.SqlGeneratorMysql;
import com.myjdbc.jdbc.core.sqlgenerator.impl.SqlGeneratorOracle;

public enum DBType {

    ORACLE("oracle", "Oracle数据库", "oracle.jdbc.OracleDriver", SqlGeneratorOracle.class),
    MYSQL("mysql", "MySql数据库", "com.mysql.jdbc.Driver", SqlGeneratorMysql.class),
    MONGODB("mongodb", "MongoDB数据库", "mongodb", SqlGeneratorMysql.class);

    private String code;
    private String remark;
    private String driver;
    private Class<? extends SqlGenerator> sqlGeneratorClass;

    DBType(String code, String remark, String driver, Class<? extends SqlGenerator> sqlGeneratorClass) {
        this.code = code;
        this.remark = remark;
        this.driver = driver;
        this.sqlGeneratorClass = sqlGeneratorClass;
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

    public SqlGenerator getSqlGenerator() {
        SqlGenerator sqlGenerator = null;
        try {
            sqlGenerator = sqlGeneratorClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sqlGenerator;
    }

}
