package com.myjdbc.core.util;


/**
 * @Author 陈文
 * @Date 2019/12/25  9:53
 * @Description 使用阿里的com.alibaba.fastjson.JSONObject，会产生错误（转换成JSONObject对象，而不是想要的实体）
 * 所以使用PojoUtil类的JSON转换
 */
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
//        bo = (T) JSONObject.parse(json);
        return PojoUtil.jsonToBo(bo, json);
    }

    /**
     * 将bo转换成Json
     *
     * @param bo
     * @param <T>
     * @return
     */
    public static <T> String BoTojson(T bo) {
//        if (bo != null) {
//            return JSONObject.toJSONString(bo);
//        }
//        return null;
        return PojoUtil.boTojson(bo);
    }

}
