package com.myjdbc.core.util.config.properties.enums;

/**
 * @Author 陈文
 * @Date 2020/05/20  12:42
 * @Description 属性配置枚举-接口
 */
public interface PropertiesEnum {

    /**
     * 配置属性名
     *
     * @return 属性名
     */
    String getCode();

    /**
     * 备注
     *
     * @return 备注
     */
    String getRemark();

    /**
     * 默认值
     *
     * @return 默认值
     */
    Object getDefaultValue();
}
