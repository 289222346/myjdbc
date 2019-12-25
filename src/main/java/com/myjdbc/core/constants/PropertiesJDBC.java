package com.myjdbc.core.constants;

public enum PropertiesJDBC {

    DBTYPE("jdbc.dbType", "数据库类型"),
    URL("jdbc.url.jeecg", "数据库连接地址"),
    USERNAME("jdbc.username.jeecg", "数据库登录名"),
    PASSWORD("jdbc.password.jeecg", "数据库登录密码"),
    MAX_ACTIVE("maxActive", "连接池中最大连接数"),
    INITIAL_SIZE("initialSize", "初始化时建立的物理连接的个数"),
    MAX_WAIT("maxWait", "获取连接时最长等待时间，单位是毫秒");


    private String code;
    private String remark;

    PropertiesJDBC(String code, String remark) {
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
