package com.myjdbc.mymongodb.util;

import com.myjdbc.core.util.ClassUtil;
import io.swagger.annotations.ApiModelProperty;
import org.bson.Document;
import org.springframework.data.annotation.Id;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MongoDBUtil {

    public static void main(String[] args) {
//        UserExample userExample = new UserExample();
//        userExample.setPhone("18676826110");
//        userExample.setName("陈文");
//        Document document = getDocument(userExample);
//        System.out.println(document);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/26  14:26
     * @Description 获取MongoDB的Document文档
     */
    public static <T> Document getDocument(T t) {
        Document document = new Document();
        Map<String, Object> map = MongoDBUtil.mongoDBPOToMap(t);
        document.putAll(map);
        return document;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/29  20:09
     * @Description 将对象属性反射成Map(Document)
     */
    public static <T> Map<String, Object> mongoDBPOToMap(T obj) {
        Class<?> cls = obj.getClass();
        // 获取该类所有属性名
        List<Field> fieldList = ClassUtil.getAllFieldsList(cls);
        //排除掉非ApiModelProperty属性
        for (int i = 0; i < fieldList.size(); i++) {
            ApiModelProperty apiModelProperty = fieldList.get(i).getAnnotation(ApiModelProperty.class);
            if (apiModelProperty == null) {
                fieldList.remove(i--);
            }
        }
        //整理后的有效属性名
        Field[] fields = fieldList.toArray(new Field[fieldList.size()]);

        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = ClassUtil.getField(field.getName());
            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);

                //属性名
                String fieldName = field.getName();
                // 把获得的值设置给map对象

                Object value = getMethod.invoke(obj);
                if (!ObjectUtils.isEmpty(value)) {
                    //若ID属性不为_id，则添加默认ID属性
                    Id id = field.getAnnotation(Id.class);//存在Id注解，则为主键，MongoDB中默认主键为_id（带索引）
                    if (id != null && !"_id".equals(fieldName)) {
                        map.put("_id", value);
                    } else {
                        map.put(fieldName, value);
                    }
                }
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }

}
