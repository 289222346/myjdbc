package com.myjdbc.redis;

import com.myjdbc.jdbc.annotation.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class MyRedisDictionary {

    @Autowired
    private MyRedisService redisService;

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:05
     * @Description 字典映射
     * 如果有字典注解，将值替换成字典映射值
     */
    private Object valueToDictionary(Class<? extends Object> cls, Field field, Object value) {
        Dictionary dictionary = field.getAnnotation(Dictionary.class);
        if (dictionary != null) {
            //从缓存中获取
            if (dictionary.fieldName().equals("")) {
                value = redisService.getDictionaryValue(dictionary.tableClass(), dictionary.replaceName().toUpperCase(), value + "");
            } else {
                value = redisService.getDictionaryValue(dictionary.tableClass(), dictionary.fieldName(), dictionary.replaceName().toUpperCase(), value + "");
            }
        }
        return value;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取字典注解
     */
    public static Dictionary getDictionary(Field field) {
        Dictionary dictionary = field.getAnnotation(Dictionary.class);
        return dictionary;
    }


//                    for (Field field : dictionaryMap.keySet()) {
//        Dictionary dictionary = dictionaryMap.get(field);
//        String copyName = BeanUtil.getPrimaryName(dictionary.tableClass(), dictionary.copyName());
//        String getField = BeanUtil.getField(copyName);
//        Method getMethod = BeanUtil.getMethod(cls, getField);
//        Object value = null;
//        value = getMethod.invoke(obj);
//
//        Method setMethod = BeanUtil.getSetMethod(cls, field, field.getType());
//        if (value != null) {
//            //字典映射
//            value = valueToDictionary(cls, field, value);
//            setMethod.invoke(obj, value);
//        }
//    }

    //字典映射
//    value = valueToDictionary(cls, field, value);


//    Dictionary dictionary = BeanUtil.getDictionary(field);
//                if (dictionary != null && StringUtil.isNotEmpty(dictionary.copyName())) {
//        dictionaryMap.put(field, dictionary);
//    }
}
