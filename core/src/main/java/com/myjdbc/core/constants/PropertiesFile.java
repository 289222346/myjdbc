package com.myjdbc.core.constants;

/**
 * @Author 陈文
 * @Date 2019/12/26  9:48
 * @Description 所有配置文件记载
 */
public enum PropertiesFile {

    JDBC("dbconfig.properties", "JDBC配置文件");

    private String code;
    private String remark;

    PropertiesFile(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}
