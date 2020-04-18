package com.myjdbc.core.constants;


public enum DBType {

    ORACLE("oracle", "Oracle数据库", "oracle.jdbc.OracleDriver"),
    MYSQL("mysql", "MySql数据库", "com.mysql.jdbc.Driver"),
    MONGODB("mongodb", "MongoDB数据库", "mongodb");

    private String code;
    private String remark;
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

//    public SqlGenerator getSqlGenerator() {
//        SqlGenerator sqlGenerator = null;
//        try {
//            sqlGenerator = sqlGeneratorClass.newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return sqlGenerator;
//    }

}
