package com.myjdbc.core.util;


import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

    /**
     * 将Json转换成Bo，只会替换Json中存在的属性
     *
     * @param bo
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T jsonToBo(T bo, String json) {
        bo = (T) JSONObject.parse(json);
        return bo;
    }

    /**
     * 将bo转换成Json
     *
     * @param bo
     * @param <T>
     * @return
     */
    public static <T> String BoTojson(T bo) {
        return JSONObject.toJSONString(bo);
    }

}
