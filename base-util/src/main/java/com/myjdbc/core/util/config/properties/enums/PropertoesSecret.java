package com.myjdbc.core.util.config.properties.enums;


import com.myjdbc.core.util.config.constants.DBType;

/**
 * @Author 陈文
 * @Date 2020/07/02  20:57
 * @Description 密码工具配置
 */
public enum PropertoesSecret implements PropertiesEnum {

    /**
     * 秘钥
     */
    SECRET_KEY("myjdbc.secret", "数据库类型", "Z8SjxH4Lc/Inebb9Zl5G9RziP6eY");


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


    PropertoesSecret(String code, String remark, Object defaultValue) {
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
