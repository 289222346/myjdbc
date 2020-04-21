package com.myjdbc.mymongodb.util;

import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bson.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MongoUtil {

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
     * @Description 获取Mongo的Document文档
     */
    public static <T> Document mongoPOJOToDocument(T t) {
        Document document = new Document(MongoUtil.mongoPOJOToMap(t));
        return document;
    }

    public static <T> T documentToMongoPOJO(Document document, Class<T> cls) {
        return mapToMongoPOJO(document, cls);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/29  20:09
     * @Description 将对象属性反射成Map(Document)
     */
    public static <T> Map<String, Object> mongoPOJOToMap(T obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = getValidFields(cls);
        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = ClassUtil.getField(field.getName());
            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                //属性名
                String propertyName = getPropertyName(field);
                // 把获得的值设置给map对象
                Object value = getMethod.invoke(obj);
                if (!ObjectUtils.isEmpty(value)) {
                    map.put(propertyName, value);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> T mapToMongoPOJO(Map<String, Object> map, Class<T> cls) {
        T t = null;
        try {
            t = cls.newInstance();
            Field[] fields = getValidFields(cls);
            for (Field field : fields) {
                // 获取要设置的属性的set方法名称
                String setField = ClassUtil.setField(field.getName());
                try {
                    // 声明类函数方法，并获取和设置该方法型参类型
                    Method setMethod = cls.getMethod(setField, field.getType());
                    //属性名
                    String propertyName = getPropertyName(field);
                    Object value = map.get(propertyName);
                    setMethod.invoke(t, value);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取属性名称
     * 如果存在{@link ApiModelProperty}注解的 ,且其{@code name}参数不为空，则用{@code name}作为属性名称
     * 如果不存在{@link ApiModelProperty}注解的 ,或者{@code name}参数为空的，则使用属性本身名字
     *
     * @param field 实体属性
     * @return 经过处理后的属性名称
     * @Author 陈文
     * @Date 2020/4/21  17:03
     */
    public static String getPropertyName(Field field) {
        Id id = field.getAnnotation(Id.class);
        //mongodb中，默认使用_id作为主键
        // 因为mongodb会自动为该字段创建索引，所以主键不管在实体中是何名称，到数据库统一使用_id
        if (id != null) {
            return "_id";
        }
        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
        /**
         * 没有模型属性，或者{@code name}参数为空，则返回属性原本的名字
         */
        if (apiModelProperty == null || StringUtil.isEmpty(apiModelProperty.name())) {
            return field.getName();
        }
        //返回模型属性重定义的名字
        return apiModelProperty.name();
    }

    /**
     * 获取模型名称(数据库表名)
     *
     * @Author: 陈文
     * @Date: 2020/4/20 11:26
     */
    public static String getModelName(Class cls) {
        ApiModel apiModel = (ApiModel) cls.getAnnotation(ApiModel.class);
        String modelName = null;
        if (apiModel != null) {
            //collectionName
            modelName = apiModel.value();
        }
        if (StringUtil.isEmpty(modelName)) {
            modelName = cls.getSimpleName();
        }
        return modelName;
    }

    /**
     * 将{@link Map}转换成 {@link Query}。
     * 内容示意：Map{key,value} 其中{@code key} 是字段名，{@code value} 是限定值。限定条件全部为EQ(完全相等)
     *
     * @Author: 陈文
     * @Date: 2020/4/20 12:40
     */
    public static Query mapToQuery(Map<String, Object> map) {
        Query query = new Query();
        map.forEach((key, value) -> {
            query.addCriteria(Criteria.where(key).is(value));
        });
        return query;
    }

    public static Field[] getValidFields(Class<?> cls) {
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
        return fieldList.toArray(new Field[fieldList.size()]);
    }

}
