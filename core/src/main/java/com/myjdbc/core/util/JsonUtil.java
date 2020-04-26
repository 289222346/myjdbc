package com.myjdbc.core.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Author 陈文
 * @Date 2019/12/25  9:53
 * @Description 使用阿里的com.alibaba.fastjson.JSONObject，会产生错误（转换成JSONObject对象，而不是想要的实体）
 * 所以使用PojoUtil类的JSON转换
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 将Json转换成Bo，只会替换Json中存在的属性
     *
     * @param cls
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T jsonToBo(Class<T> cls, String json) {
        try {
            return objectMapper.readValue(json, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将bo转换成Json
     *
     * @param bo
     * @param <T>
     * @return
     */
    public static <T> String boToJson(T bo) {
        String str = "";
        try {
            str = objectMapper.writeValueAsString(bo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
