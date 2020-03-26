package com.myjdbc.redis.service.impl;

import com.myjdbc.core.util.StringUtil;
import com.myjdbc.jdbc.util.BeanUtil;
import com.myjdbc.redis.annotation.Dictionary;
import com.myjdbc.redis.service.MyRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class MyRedisDictionary {

    @Autowired
    private MyRedisService myRedisService;

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:05
     * @Description 字典映射
     * 如果有字典注解，将值替换成字典映射值
     */
    public <T> Object valueToDictionary(Class<T> cls, Field field, Object value, T t) {
        Dictionary dictionary = field.getAnnotation(Dictionary.class);
        if (dictionary != null) {
            if (!dictionary.copyName().equals("")) {
                //注解的copyName属性不为空，则将指定属性赋值给value
                value = BeanUtil.getValue(t, dictionary.copyName(), cls);
            }
            if (!StringUtil.isNotEmpty(value)) {
                return null;
            }
            //从缓存中获取
            if (dictionary.fieldName().equals("")) {
                Object id = value;
                if (!dictionary.copyName().equals("")) {
                    id = BeanUtil.getPrimaryValue(t, dictionary.copyName());
                }
                String replaceName = BeanUtil.getSqlFormatName(cls, dictionary.replaceName());
// .toUpperCase()
                //通过Id查找
                value = myRedisService.getDictionaryValue(dictionary.tableClass(), replaceName, id + "");
            } else {
                //待鉴定
                value = myRedisService.getDictionaryValue(dictionary.tableClass(), dictionary.fieldName(), dictionary.replaceName().toUpperCase(), value + "");
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
