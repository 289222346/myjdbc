package com.myjdbc.core.util;

import com.myjdbc.core.constants.PropertiesFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author 陈文
 * @Date 2020/3/26  11:19
 * @return
 * @Description 属性配置工具，用与获取配置信息
 */
public class PropertiesUtil {

    private Properties prop;

    public PropertiesUtil(PropertiesFile propertiesFile) {
        this.prop = getProperties(propertiesFile.getCode());
    }

    /**
     * @Author 陈文
     * @Date 2019/12/26  9:46
     * @Description 读取属性
     */
    public String readProperty(String key) {
        String value = prop.getProperty(key);
        return value;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/26  9:44
     * @Description 读取属性
     * 适用于简单的，只获取一个属性或者少量属性的情况
     */
    public static String readProperty(String fileName, String key) {
        Properties prop = getProperties(fileName);
        String value = prop.getProperty(key);
        return value;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/26  9:44
     * @Description 获取属性文件
     */
    private static Properties getProperties(String fileName) {
        //读取属性文件x.properties
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            prop.load(in);///加载属性列表
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

}
