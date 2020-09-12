package com.myjdbc.core.util.config.properties.enums;

/**
 * @Author 陈文
 * @Date 2019/12/26  9:48
 * @Description 所有配置文件记载
 */
public interface PropertiesFileI {


    public String getFileName();

    public String getRemark();

    public Enum<? extends PropertiesEnum>[] getPropertiesEnums();
}
