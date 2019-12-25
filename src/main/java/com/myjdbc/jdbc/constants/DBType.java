package com.myjdbc.jdbc.constants;

public enum DBType {

    ORACLE("oracle", "Oracle数据库", "oracle.jdbc.driver.OracleDriver", "sqlGeneratorOracle"),
    MYSQL("mysql", "MySql数据库", "com.mysql.jdbc.Driver", "sqlGeneratorMysql");

    private String code;
    private String remark;
    private String driver;
    private String generator;

    DBType(String code, String remark, String driver, String generator) {
        this.code = code;
        this.remark = remark;
        this.driver = driver;
        this.generator = generator;
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

    public String getGenerator() {
        return generator;
    }
}
