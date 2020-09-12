package com.myjdbc.core.util.config.properties.enums;

/**
 * @Author 陈文
 * @Date 2019/12/26  9:48
 * @Description 所有配置文件记载
 */
public enum PropertiesFile implements PropertiesFileI {

    /**
     * JDBC配置
     */
    JDBC("application.properties", "JDBC配置", PropertiesJDBC.values()),
    /**
     * ID生成器配置
     */
    ID_GENERATOR("application.properties", "ID生成器配置", PropertiesIdGenerator.values()),
    /**
     * 秘钥工具配置
     */
    SECRET("application.properties", "秘钥工具配置", PropertoesSecret.values());

    /**
     * 配置文件名
     */
    private String fileName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 属性配置枚举
     */
    private Enum<? extends PropertiesEnum>[] propertiesEnums;

    PropertiesFile(String fileName, String remark, Enum<? extends PropertiesEnum>[] propertiesEnums) {
        this.fileName = fileName;
        this.remark = remark;
        this.propertiesEnums = propertiesEnums;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public Enum<? extends PropertiesEnum>[] getPropertiesEnums() {
        return propertiesEnums;
    }
}
