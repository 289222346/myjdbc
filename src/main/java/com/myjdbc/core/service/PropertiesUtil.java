package com.myjdbc.core.service;

import com.myjdbc.core.constants.PropertiesFile;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {


    private Properties prop;

    public PropertiesUtil(@NotNull PropertiesFile propertiesFile) {
        this.prop = getProperties(propertiesFile.getCode());
    }

    /**
     * @Author 陈文
     * @Date 2019/12/26  9:46
     * @Description 读取属性
     */
    public String readProperty(@NotNull String key) {
        String value = prop.getProperty(key);
        return value;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/26  9:44
     * @Description 读取属性
     * 适用于简单的，只获取一个属性或者少量属性的情况
     */
    public static String readProperty(@NotNull String fileName, @NotNull String key) {
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
