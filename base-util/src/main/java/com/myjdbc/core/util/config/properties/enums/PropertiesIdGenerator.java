package com.myjdbc.core.util.config.properties.enums;


/**
 * @Author 陈文
 * @Date 2020/05/02  23:18
 * @Description ID自动生成器配置
 */
public enum PropertiesIdGenerator implements PropertiesEnum {

    /**
     * 雪花-数据中心编码
     */
    DATA_CENTER_ID("myjdbc.id.datacenter", "雪花-数据中心编码", 0),
    /**
     * 雪花-机器编码
     */
    MACHINE_ID("myjdbc.id.machine", "雪花-机器编码", 0);

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


    PropertiesIdGenerator(String code, String remark, Object defaultValue) {
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
