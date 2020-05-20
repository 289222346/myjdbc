package com.myjdbc.core.config.properties;


import com.myjdbc.core.config.properties.enums.PropertiesEnum;
import com.myjdbc.core.config.properties.enums.PropertiesFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author 陈文
 * @Date 2020/3/26  11:19
 * @return
 * @Description 属性配置工具，用与获取配置信息
 */
public class PropertiesConfigUtil {

    /**
     * 属性配置池
     */
    private static final Map<String, Properties> PROP_NAME_POOL = new HashMap<>();

    /**
     * 属性赋值池
     */
    private final Map<String, Object> PROP_VALUE_POOL = new HashMap<>();

    /**
     * 实例化工具后，使用 {@link #readProperty}方法获取属性配置
     *
     * @param propertiesFile 配置枚举（分类）
     */
    public PropertiesConfigUtil(PropertiesFile propertiesFile) {
        Properties prop = getProperties(propertiesFile.getFileName());
        PropertiesEnum[] propertiesEnums = (PropertiesEnum[]) propertiesFile.getPropertiesEnums();
        for (PropertiesEnum propertiesEnum : propertiesEnums) {
            String key = propertiesEnum.getCode();
            Object value = prop.getProperty(key);
            //如果属性未配置，则取默认值
            if (value == null) {
                value = propertiesEnum.getDefaultValue();
            }
            PROP_VALUE_POOL.put(key, value);
        }
    }

    /**
     * 读取属性
     *
     * @param key 属性名
     * @author 陈文
     * @date 2019/12/26  9:46
     */
    public Object readProperty(String key) {
        return PROP_VALUE_POOL.get(key);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/26  9:44
     * @Description 获取属性文件
     */
    private static Properties getProperties(String fileName) {
        //属性池中有，则优先取出
        if (PROP_NAME_POOL.containsKey(fileName)) {
            return PROP_NAME_POOL.get(fileName);
        }

        //读取属性文件x.properties
        Properties prop = new Properties();
        InputStream in = PropertiesConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            //文件不存在，认为所有属性为空
            if (in != null) {
                //加载属性列表
                prop.load(in);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PROP_NAME_POOL.put(fileName, prop);
        return prop;
    }

}
