package com.myjdbc.jdbc.constants;

public enum DBType {

    ORACLE(0, "oracle", "Oracle数据库"),
    MYSQL(1, "mysql", "MySql数据库");

    private int code;
    private String remark;
    private String value;

    DBType(int code, String value, String remark) {
        this.code = code;
        this.remark = remark;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    public String getValue() {
        return value;
    }
}
